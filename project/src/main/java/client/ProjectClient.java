package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.comm.*;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;

import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectClient {
    private static final Logger logger = Logger.getLogger(ProjectClient.class.getName());

    private final ManagedChannel channel;
    private final CommunicationServiceGrpc.CommunicationServiceBlockingStub blockingStub;
    private final CommunicationServiceGrpc.CommunicationServiceStub nonBlockingStub;

    final CountDownLatch done = new CountDownLatch(1);

    private String myIP;
    private String toIP;

    /**
     * Construct client connecting to ProjectServer at {@code host:port}.
     */
    public ProjectClient(String host, int port) {
        this.channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();
        this.blockingStub = CommunicationServiceGrpc.newBlockingStub(this.channel);
        this.nonBlockingStub = CommunicationServiceGrpc.newStub(this.channel);

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
        this.blockingStub = CommunicationServiceGrpc.newBlockingStub(this.channel);
        this.nonBlockingStub = CommunicationServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * ping server.
     */
    public void ping() {
        logger.info("ping " + this.toIP + " ...");
        // Build PingRequest
        PingRequest pingRequest = PingRequest.newBuilder().setMsg("ping from " + myIP).build();

        // Build Request
        Request request = Request.newBuilder()
                .setFromSender(this.myIP)
                .setToReceiver(this.toIP)
                .setPing(pingRequest)
                .build();

        Response response;
        try {
            response = blockingStub.ping(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info(response.getCode().toString());
    }

    /**
     * PutHandler
     */
    public void putHandler() {

    }

    /**
     * GetHandler
     */
    public void getHandler() {
        logger.info("getHandler " + this.toIP + " ...");
        // TODO: Build Get Request
        GetRequest getRequest = GetRequest
                .newBuilder()
                .build();

        // Build Request
        Request request = Request
                .newBuilder()
                .setFromSender(this.myIP)
                .setToReceiver(this.toIP)
                .setGetRequest(getRequest)
                .build();

        try {
            nonBlockingStub.getHandler(request, new ClientResponseObserver<Request, Response>() {
                ClientCallStreamObserver<Request> requestStream;

                @Override
                public void onNext(Response value) {
                    logger.info(value.getDatFragment().getData().toStringUtf8());
                    requestStream.request(1);
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    done.countDown();
                }

                @Override
                public void onCompleted() {
                    logger.info("All Done");
                    done.countDown();
                }

                @Override
                public void beforeStart(ClientCallStreamObserver<Request> requestStream) {
                    this.requestStream = requestStream;
                    requestStream.disableAutoInboundFlowControl();

                    requestStream.setOnReadyHandler(new Runnable() {
                        @Override
                        public void run() {
                            // Start generating values from where we left off on a non-gRPC thread.
                            // TODO: build request
//                            Request request = Request.newBuilder().build();
                            // Send request
                            while (requestStream.isReady()) {
                                requestStream.onNext(request);
                                requestStream.onCompleted();
                            }
                        }
                    });
                }
            });
            done.await();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info("getHandler DONE");
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
            /* Access a service running on the local machine on port */
            client.ping();
            client.getHandler();
        } finally {
            client.shutdown();
        }
    }
}
