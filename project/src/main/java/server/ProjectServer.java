package server;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import config.ServerConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

import io.grpc.comm.*;
import io.grpc.stub.StreamObservers;

// ProjectServer is similar to Node
public class ProjectServer {
    private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());

    private int server_id; // server id is same as node id
    private int external_port; // port use for team2team communication
    private int internal_port; // port use for node2node communication

    LinkedBlockingQueue<Request> incomming_queue; // use for buffer incoming Request
    LinkedBlockingQueue<Response> outgoing_queue; // use for buffer outgoing Response

    private Server server;

    public ProjectServer(int server_id, int external_port, int internal_port) {
        this.server_id = server_id;
        this.external_port = external_port;
        this.internal_port = internal_port;
    }

    public ProjectServer(String config_file_path) {
        Gson gson = new Gson();
        try {
            ServerConfig config = gson.fromJson(new FileReader(config_file_path), ServerConfig.class);
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
        /* The port on which the server should run */
        server = ServerBuilder.forPort(this.external_port).addService(new CommunicationServiceImpl()).build().start();
        logger.info("Server started, listening on " + this.external_port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown
                // hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ProjectServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // default value
        int server_id = 1;
        int external_port = 8080;
        int internal_port = 8081;
        String config_file_path = null;

        switch (args.length) {
            case 3:
                internal_port = Integer.parseInt(args[2]);
                // no break intentionally
            case 2:
                external_port = Integer.parseInt(args[1]);
                // no break intentionally
            case 1:
                try {
                    server_id = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    config_file_path = args[0];
                }
            case 0:
                // no break intentionally
                break;
            default:
                logger.info("use [server_id | config_file_path [external_port [internal_port] ] ]");
                System.exit(-1);
        }

        ProjectServer server;
        if (config_file_path == null) {
            server = new ProjectServer(server_id, external_port, internal_port);
        } else {
            server = new ProjectServer(config_file_path);
        }

        server.start();
        server.blockUntilShutdown();
    }

    // Service class implementation
    static class CommunicationServiceImpl extends CommunicationServiceGrpc.CommunicationServiceImplBase {
        private static final Logger logger = Logger.getLogger(CommunicationServiceImpl.class.getName());

        @Override
        public void ping(Request request, StreamObserver<Response> responseObserver) {
            logger.info("ping from " + request.getFromSender());

            // create a Response Builder, use this builder to build a Response
            Response response =
                    Response.newBuilder()
                            .setCode(StatusCode.Ok)
                            .setMsg(String.valueOf(System.currentTimeMillis()))
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
                        // TODO: Accept and enqueue the request.


                        // TODO: Do work here (maybe send a response).


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
                    // End the response stream if the client presents an error.
                    t.printStackTrace();
                    responseObserver.onCompleted();
                }

                @Override
                public void onCompleted() {
                    // Signal the end of work when the client ends the request stream.
                    logger.info("putHandler COMPLETED");
                    responseObserver.onCompleted();
                }
            };
        }

        @Override
        public void getHandler(Request request, StreamObserver<Response> responseObserver) {
            logger.info("getHandler from " + request.getFromSender());
            // TODO: process the request and get the data

            // create uuid for a file, fragment that file, count number of fragments
            String uuid = "uuid";
            int numberOfFragment = 3;
            int mediaType = 1;

            // example of fragment
            LinkedList<String> list = new LinkedList<>();
            for (int i  = 0; i < numberOfFragment; ++i) {
                list.add("fragment number " + i);
            }

            // first response is about metaData
            Response response =
                    Response.newBuilder()
                            .setCode(StatusCode.Ok)
                            .setMsg("Meta")
                            .setMetaData(
                                    MetaData.newBuilder()
                                            .setUuid("uuid")
                                            .setNumOfFragment(3)
                                            .setMediaType(1)
                                            .build()
                            )
                            .build();

            // send first package
            logger.info("sending meta data ...");
            responseObserver.onNext(response);

            // send all fragments
            for (String str : list) {
                response = Response.newBuilder()
                        .setCode(StatusCode.Ok)
                        .setMsg("Data")
                        .setDatFragment(
                                DatFragment.newBuilder()
                                        .setData(ByteString.copyFromUtf8(str))
                                        .build()
                        )
                        .build();

                // send fragment
                logger.info("sending data " + response.getDatFragment().getData().toString());
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();
            logger.info("getHandler DONE");
        }
    }
}
