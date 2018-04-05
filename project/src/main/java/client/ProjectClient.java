package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.comm.CommunicationServiceGrpc;
import io.grpc.comm.PingRequest;
import io.grpc.comm.Request;
import io.grpc.comm.Response;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectClient {
    private static final Logger logger = Logger.getLogger(ProjectClient.class.getName());

    private final ManagedChannel channel;
    private final CommunicationServiceGrpc.CommunicationServiceBlockingStub blockingStub;

    private String myIP;
    private String toIP;

    /**
     * Construct client connecting to ProjectServer at {@code host:port}.
     */
    public ProjectClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
        this.toIP = host;
        try {
            this.myIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Construct client for accessing server using the existing channel.
     */
    ProjectClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = CommunicationServiceGrpc.newBlockingStub(channel);
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost"; // default host;
        int port = 8080; // default port

        // if host or port are supplied, use them
        switch (args.length) {
            case 2:
                port = Integer.parseInt(args[1]);
                // fall over to case 1
                // no break intentionally
            case 1:
                host = args[0];
                break;
        }

        ProjectClient client = new ProjectClient(host, port);
        try {
            /* Access a service running on the local machine on port 50051 */
            client.ping();
        } finally {
            client.shutdown();
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * pring server.
     */
    public void ping() {
        logger.info("Trying to ping " + this.toIP + " ...");
        // Build PingRequest
        PingRequest pingRequest = PingRequest.newBuilder().setMsg("ping from " + myIP).build();

        // Build Request
        Request.Builder requestBuilder = Request.newBuilder();
        requestBuilder.setFromSender(this.myIP);
        requestBuilder.setToReceiver(this.toIP);
        requestBuilder.setPing(pingRequest);
        Request request = requestBuilder.build();

        Response response;
        try {
            response = blockingStub.ping(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getCode().toString());
    }
}
