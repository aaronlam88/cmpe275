package server;

import com.cmpe275.grpcComm.*;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import config.ServerConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.election.Vote;
import io.grpc.election.ElectionMsg;
import io.grpc.election.ElectionReply;
import io.grpc.election.ElectionServiceGrpc;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.InetAddress;
/**
 * ProjectServer is similar to Node, ProcessingNode concept
 * A ProjectServer has:
 * variables:
 * server_id: default 1, as the id of node in a cluster
 * external_port: default 8080, use to accept grpc call from external client (cluster to cluster)
 * internal_port: default 8081, use to accept grpc call from internal client (node to node)
 * election_cycle: default 1, current election term
 * message objects:
 * externalServer: a Server created using external_port
 * internalServer: a Server created using internal_port
 * rountingTable: a HashMap of <server_id, InternalClient> use to call grpc func of other ProjectServer
 * work handler objects:
 * electionManager: handle leader election process
 * databaseManager: handle database insertion and selection
 * taskManager: distribute tasks to other nodes
 */
public class ProjectServer {
    private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());

    private static final int fragmentSize = 1024000; // 1,024,000 char ~= 1MB

    private int server_id; // server id is same as node id
    private int external_port; // port use for team2team communication
    private int internal_port; // port use for node2node communication
    private int election_cycle = 1;

    private Server externalServer;
    private Server internalServer;

    private HashMap<Integer, InternalClient> routingTable = new HashMap<>();

    private DatabaseManager databaseManager;
    private ElectionManager electionManager;
    private TaskManager taskManager;

    //private Timer timer; // for timeout follower state

    private ProjectServer(int server_id, int external_port, int internal_port) {
        this.server_id = server_id;
        this.external_port = external_port;
        this.internal_port = internal_port;
    }

    private ProjectServer(String server_config_file_path) {
        Gson gson = new Gson();
        try {
            ServerConfig config = gson.fromJson(new FileReader(server_config_file_path), ServerConfig.class);
            this.server_id = config.server_id;
            this.internal_port = config.internal_port;
            this.external_port = config.external_port;
        } catch (Exception e) {
            logger.info(e.getMessage());
            System.exit(-1);
        }
    }

    // default
    public ProjectServer() {
        this.server_id = 1;
        this.external_port = 8080;
        this.internal_port = 8081;
    }

    private void start() throws IOException {
        logger.info("Start Server [" + server_id + "]");
        // create externalServer
        externalServer = ServerBuilder
                .forPort(this.external_port)
                .addService(new CommunicationServiceImpl(databaseManager, taskManager))
                .build()
                .start();
        logger.info("External Server started, listening on " + this.external_port);
        // create internalServer
        internalServer = ServerBuilder
                .forPort(this.internal_port)
                .addService(new CommunicationServiceImpl(databaseManager, taskManager))
                .addService(new ElectionServiceImpl(electionManager))
                .build()
                .start();
        logger.info("Internal Server started, listening on " + this.internal_port);

        //electionManager.startCountDown();
        // handle shutdown
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ProjectServer.this.stop();
                System.err.println("*** server shut down");
            }
        });

    }

    private void stop() {
        if (externalServer != null) {
            externalServer.shutdown();
        }
        if (internalServer != null) {
            internalServer.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (externalServer != null) {
            externalServer.awaitTermination();
        }
        if (internalServer != null) {
            internalServer.awaitTermination();
        }
    }

    public void addNodeToNetwork(int nodeID, InternalClient toNode) {
      routingTable.put(nodeID, toNode);
    }
    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // default value
        int server_id = 1;
        int external_port = 8080;
        int internal_port = 8081;
        String server_config_file_path = null;
        String db_config_file_path = null;
        String ips_config_file_path = "../src/main/resources/ips.json"; //the path to all network nodes

        switch (args.length) {
            case 3:
                internal_port = Integer.parseInt(args[2]);
                // no break intentionally
            case 2:
                try {
                    external_port = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    db_config_file_path = args[1];
                }
                // no break intentionally
            case 1:
                try {
                    server_id = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    server_config_file_path = args[0];
                }
            case 0:
                // no break intentionally
                break;
            default:
                logger.info("use [server_id | server_config_file_path [external_port [internal_port] ] ]");
                System.exit(-1);
        }

        ProjectServer server;
        if (server_config_file_path == null) {
            server = new ProjectServer(server_id, external_port, internal_port);
        } else {
            server = new ProjectServer(server_config_file_path);
        }

        if (db_config_file_path != null) {
            server.databaseManager = new DatabaseManager(db_config_file_path);
        } else {
            // default
            server.databaseManager = new DatabaseManager("cmpe275", "cmpe275!", "jdbc:mysql://localhost:3306/cmpe275?autoReconnect=true&useSSL=false");
        }

        //test on one system; use ports for different servers
        String to_ip = InetAddress.getLocalHost().getHostAddress();
        logger.info(to_ip);
        Gson gson = new Gson();
        NodeJson[] nodesArray = gson.fromJson(new FileReader("./src/main/resources/ips.json"), NodeJson[].class);

        for (NodeJson node : nodesArray) {
          //logger.info("Before, " +node.nodeID +" has been added to routingTable");
          if (node.to_port != internal_port) {
            InternalClient nodeInNetwork = new InternalClient(to_ip, node.to_port);
            server.addNodeToNetwork(node.nodeID, nodeInNetwork);
            logger.info("After, " + node.nodeID +" has been added to routingTable");
          }
        }

        server.electionManager = new ElectionManager();

        server.start();
        server.blockUntilShutdown();
    }

    // CommunicationService class implementation
    static class CommunicationServiceImpl extends CommunicationServiceGrpc.CommunicationServiceImplBase {
        private static final Logger logger = Logger.getLogger(CommunicationServiceImpl.class.getName());
        private static DatabaseManager databaseManager;
        private static TaskManager taskManager;

        CommunicationServiceImpl(DatabaseManager databaseManager, TaskManager taskManager) {
            this.databaseManager = databaseManager;
            this.taskManager = taskManager;
        }

        @Override
        public void ping(Request request, StreamObserver<Response> responseObserver) {
            logger.info("ping from " + request.getFromSender());

            // create a Response Builder, use this builder to build a Response
            Response response =
                    Response.newBuilder()
                            .setCode(StatusCode.Ok)
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public StreamObserver<Request> putHandler(StreamObserver<Response> responseObserver) {
            // Set up manual flow control for the request stream
            final ServerCallStreamObserver<Response> serverCallStreamObserver = (ServerCallStreamObserver<Response>) responseObserver;
            serverCallStreamObserver.disableAutoInboundFlowControl();

            // Guard against spurious onReady() calls caused by a race between onNext() and onReady(). If the transport
            // toggles isReady() from false to true while onNext() is executing, but before onNext() checks isReady(),
            // request(1) would be called twice - once by onNext() and once by the onReady() scheduled during onNext()'s
            // execution.
            final AtomicBoolean wasReady = new AtomicBoolean(false);

            serverCallStreamObserver.setOnReadyHandler(new Runnable() {
                public void run() {
                    if (serverCallStreamObserver.isReady() && wasReady.compareAndSet(false, true)) {
                        logger.info("putHandler READY");
                        serverCallStreamObserver.request(1);
                    }
                }
            });

            // Give gRPC a StreamObserver that can observe and process incoming requests.
            return new StreamObserver<Request>() {
                @Override
                public void onNext(Request request) {
                    // Process the request and send a response or an error.
                    try {
                        // insert string or strings to database
                        logger.info(request.getPutRequest().getDatFragment().getData().toStringUtf8() + "\n");
                        databaseManager.addToBatch(request.getPutRequest().getDatFragment().getData().toStringUtf8());

                        // Check the provided ServerCallStreamObserver to see if it is still ready to accept more messages.
                        if (serverCallStreamObserver.isReady()) {
                            // Signal the sender to send another request. As long as isReady() stays true, the server will keep
                            // cycling through the loop of onNext() -> request()...onNext() -> request()... until either the client
                            // runs out of messages and ends the loop or the server runs out of receive buffer space.
                            //
                            // If the server runs out of buffer space, isReady() will turn false. When the receive buffer has
                            // sufficiently drained, isReady() will turn true, and the serverCallStreamObserver's onReadyHandler
                            // will be called to restart the message pump.
                            serverCallStreamObserver.request(1);
                        } else {
                            // If not, note that back-pressure has begun.
                            wasReady.set(false);
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        responseObserver.onError(
                                Status.UNKNOWN.withDescription("Error handling request").withCause(throwable).asException());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    // End the response stream if the client  presents an error.
                    t.printStackTrace();
                    responseObserver.onCompleted();
                }

                @Override
                public void onCompleted() {
                    // need to commit the current batch to database before finish
                    databaseManager.commitBatch();

                    // Send a response to let client know
                    responseObserver.onNext(Response.newBuilder().setCode(StatusCode.Ok).setMsg("DONE").build());

                    // Signal the end of work when the client ends the request stream.
                    responseObserver.onCompleted();
                    logger.info("putHandler COMPLETED");
                }
            };
        }

        @Override
        public void getHandler(Request request, StreamObserver<Response> responseObserver) {
            String from_utc = request.getGetRequest().getQueryParams().getFromUtc();
            String to_utc = request.getGetRequest().getQueryParams().getToUtc();

            ResultSet resultSet = databaseManager.selectByTimeRanch(from_utc, to_utc);

            if (resultSet == null) {
                Response response = Response
                        .newBuilder()
                        .setCode(StatusCode.Failed)
                        .setMsg("NO RESULT")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            // create uuid for a file, fragment that file, count number of fragments
//            String uuid = "uuid";
//            int numberOfFragment = 3;
//            int mediaType = 1;

            // send all fragments
            try {
                ResultSetMetaData metadata = resultSet.getMetaData();
                int numberOfColumns = metadata.getColumnCount();

                StringBuffer fragment = new StringBuffer();
                while (resultSet.next()) {
                    StringBuffer row = new StringBuffer();
                    for (int i = 1; i <= numberOfColumns; ++i) {
                        if (i == 1) {
                            row.append(resultSet.getString(i));
                        } else if (i == 2) {
                            row.append(resultSet.getTimestamp(i));
                        } else {
                            row.append(resultSet.getDouble(i));
                        }
                        row.append(", ");
                    }
                    fragment.append(row.append("\n"));

                    if (fragment.length() >= fragmentSize) {
                        DatFragment datFragment = DatFragment
                                .newBuilder()
                                .setData(ByteString.copyFromUtf8(fragment.toString()))
                                .build();

                        Response response = Response
                                .newBuilder()
                                .setDatFragment(datFragment)
                                .build();

                        responseObserver.onNext(response);
                        fragment = new StringBuffer();
                    }
                }
                // send the last fragment
                if (fragment.length() != 0) {
                    DatFragment datFragment = DatFragment
                            .newBuilder()
                            .setData(ByteString.copyFromUtf8(fragment.toString()))
                            .build();

                    Response response = Response
                            .newBuilder()
                            .setDatFragment(datFragment)
                            .build();

                    responseObserver.onNext(response);
                }
            } catch (SQLException e) {
                logger.log(Level.WARNING, "SQLException ", e.getMessage());
            }
            responseObserver.onCompleted();
            logger.info("getHandler DONE");
        }
    }

    // Handle when receive a request from other service.
    static class ElectionServiceImpl extends ElectionServiceGrpc.ElectionServiceImplBase {
        private ElectionManager electionManager;
        //private NodeStatus senderNodeStatus;

        ElectionServiceImpl(ElectionManager electionManager) {
          this.electionManager = electionManager;
        }
        //receive heartbeat message if the sender is leader then acknowledge its node timeout update; otherwise ignore
        @Override
        public void sendHeartbeat(ElectionMsg request, StreamObserver<ElectionReply> responseObserver) {
          logger.info("get message from " + request.getFromSender());
          electionManager.receiveHeartBeat();
          // if (request.getType() == Type.Heartbeat) {
          //   electionManager.resetTimer();
          // }
          // create a Response Builder, use this builder to build a Response
          ElectionReply response =
                  ElectionReply.newBuilder()
                          .setVote(Vote.Success)
                          .build();

          responseObserver.onNext(response);
          responseObserver.onCompleted();
        }

        //node state should be candidate; and if itself's node is in the state of follower, send back vote success; otherwise, failure;
        @Override
        public void runElection(ElectionMsg request, StreamObserver<ElectionReply> responseObserver) {

        }
    }
}
