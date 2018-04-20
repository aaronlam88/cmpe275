package server;

// grpc proto import
import com.cmpe275.grpcComm.*;
import io.grpc.election.*;
import io.grpc.internal.*;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;

import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalClient {
    private static final Logger logger = Logger.getLogger(InternalClient.class.getName());

    private final ManagedChannel channel;
    private final CommunicationServiceGrpc.CommunicationServiceBlockingStub blockingStub;
    private final CommunicationServiceGrpc.CommunicationServiceStub nonBlockingStub;

    final CountDownLatch done = new CountDownLatch(1);

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
        logger.info("putHandler " + this.toIP + " ...");
        StreamObserver<Response> responseObserver = new StreamObserver<Response>() {

            @Override
            public void onNext(Response value) {
                logger.info(value.getDatFragment().getData().toStringUtf8());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("All Done for put handler");
            }
        };

        StreamObserver<Request> requestObserver = nonBlockingStub.putHandler(responseObserver);

        try {
            // create uuid for a file, fragment that file, count number of fragments
            String uuid = "uuid";
            int numberOfFragment = 3;
            int mediaType = 1;

            MetaData metaData = MetaData
                    .newBuilder()
                    .setUuid(uuid)
                    .setNumOfFragment(numberOfFragment)
                    .setMediaType(mediaType)
                    .build();

            // example of fragment
            LinkedList<String> list = new LinkedList<>();
            for (int i = 0; i < numberOfFragment; ++i) {
                list.add("fragment number " + i);
            }

            PutRequest putRequest = PutRequest
                    .newBuilder()
                    .setMetaData(metaData)
                    .build();

            Request request = Request
                    .newBuilder()
                    .setFromSender(this.myIP)
                    .setToReceiver(this.toIP)
                    .setPutRequest(putRequest)
                    .build();

            // send first meta data
            logger.info("sending meta data ...");
            requestObserver.onNext(request);

            // send all fragments
            for (String str : list) {
                DatFragment datFragment = DatFragment
                        .newBuilder()
                        .setTimestampUtc(Instant.now().toString())
                        .setData(ByteString.copyFromUtf8(str))
                        .build();

                request = Request
                        .newBuilder()
                        .setPutRequest(
                                PutRequest
                                        .newBuilder()
                                        .setMetaData(metaData)
                                        .setDatFragment(datFragment)
                                        .build()
                        )
                        .build();

                // send fragment
                logger.info("sending data " + request.getPutRequest().getDatFragment().toString());
                requestObserver.onNext(request);
            }

            // send completed
            requestObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        }
        logger.info("getHandler DONE");
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

}

