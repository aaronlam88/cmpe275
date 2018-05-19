package server;

import com.cmpe275.grpcComm.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalClient {
    private static final Logger logger = Logger.getLogger(InternalClient.class.getName());

    private static final int fragmentSize = 1024000; // 1,024,000 char ~= 1MB
    final CountDownLatch done = new CountDownLatch(1);
    private final ManagedChannel channel;
    private final CommunicationServiceGrpc.CommunicationServiceBlockingStub blockingStub;
    private final CommunicationServiceGrpc.CommunicationServiceStub nonBlockingStub;
    private String myIP;
    private String toIP;

    /**
     * Construct client connecting to ProjectServer at {@code host:port}.
     */
    public InternalClient(String host, int port) {
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
    public InternalClient(ManagedChannel channel) {
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
            long time = System.currentTimeMillis();
            response = blockingStub.ping(request);
            logger.info("Respont: " + (System.currentTimeMillis() - time) + " ms");
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        }
    }

    /**
     * PutHandler
     */
    public void putHandler(Request request) {
        logger.info("putHandler " + this.toIP + " ...");
        StreamObserver<Response> responseObserver = new StreamObserver<Response>() {

            @Override
            public void onNext(Response value) {
                logger.info(value.getDatFragment().getData().toStringUtf8());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                done.countDown();
            }

            @Override
            public void onCompleted() {
                logger.info("Completed");
                done.countDown();
            }
        };

        StreamObserver<Request> requestObserver = nonBlockingStub.putHandler(responseObserver);

        try {
            requestObserver.onNext(request); // send data fragment to server
            // send completed
            requestObserver.onCompleted();
            done.await();
        } catch (Exception e) {
            requestObserver.onError(e);
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
        }
        logger.info("putHandler DONE");
    }

    /**
     * GetHandler
     */
    public String getHandler(String from_utc, String to_utc) {
        StringBuffer stringBuffer = new StringBuffer();
        logger.info("getHandler " + this.toIP + " ...");
        // Build Get Request
        GetRequest getRequest = GetRequest
                .newBuilder()
                .setQueryParams(
                        QueryParams
                                .newBuilder()
                                .setFromUtc(from_utc)
                                .setToUtc(to_utc).build()
                )
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
                final AtomicBoolean wasReady = new AtomicBoolean(false);
                ClientCallStreamObserver<Request> requestStream;

                @Override
                public void onNext(Response value) {
                    logger.info(value.getDatFragment().getData().toStringUtf8());
                    stringBuffer.append(value.getDatFragment().getData().toStringUtf8());
                    if (requestStream.isReady()) {
                        requestStream.request(1);
                    } else {
                        wasReady.set(false);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    t.printStackTrace();
                    done.countDown();
                }

                @Override
                public void onCompleted() {
                    logger.info("All Done for get handler");
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
                            // Send request
//                            while (requestStream.isReady()) {
//                                requestStream.onNext(request);
//                                requestStream.onCompleted();
//                            }
                            if (requestStream.isReady() && wasReady.compareAndSet(false, true)) {
                                logger.info("getHandler READY");
                                requestStream.request(1);
                            }
                        }
                    });
                }
            });
            done.await();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
        }
        return stringBuffer.toString();
    }
}
