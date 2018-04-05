package server;

import com.google.gson.Gson;
import config.ServerConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.comm.CommunicationServiceGrpc;
import io.grpc.comm.Request;
import io.grpc.comm.Response;
import io.grpc.comm.UploadStatusCode;
import io.grpc.stub.StreamObserver;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

// ProjectServer is similar to Node
public class ProjectServer {
    private static final Logger logger = Logger.getLogger(ProjectServer.class.getName());

    private int server_id; // server id is same as node id
    private int external_port; // port use for team2team communication
    private int internal_port; // port use for node2node communication

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

    private void start() throws IOException {
        /* The port on which the server should run */
        server = ServerBuilder.forPort(this.external_port).addService(new PingImpl()).build().start();
        logger.info("Server started, listening on " + this.external_port);
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

    static class PingImpl extends CommunicationServiceGrpc.CommunicationServiceImplBase {
        @Override
        public void ping(Request request, StreamObserver<Response> responseObserver) {
            logger.info("Get ping from " + request.getFromSender());

            // create a Response Builder, use this builder to build a Response
            Response response =
                    Response.newBuilder()
                            .setCode(UploadStatusCode.Ok)
                            .setMsg(String.valueOf(System.currentTimeMillis()))
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

            // TODO: handle error
            // responseObserver.onError(); // we will ignore error for now
        }
    }
}
