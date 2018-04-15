package client;

import com.google.protobuf.ByteString;
import com.google.type.Date;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.comm.*;
import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedList;
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
            int numberOfFragment = 1;
            int mediaType = 1;

            MetaData metaData = MetaData
                    .newBuilder()
                    .setUuid(uuid)
                    .setNumOfFragment(numberOfFragment)
                    .setMediaType(mediaType)
                    .build();

            // example of fragment
            LinkedList<String> list = new LinkedList<>();
            list.add("BULLF  20180316/2230      8.00    37.52  -110.73  1128.00 -9999.00     3.73 -9999.00     8.19 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00\n" +
                    "    CCD  20180316/2215      8.00    40.69  -111.59  2743.00    26.27     8.82   176.10    17.01 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00\n" +
                    "    CCD  20180316/2230      8.00    40.69  -111.59  2743.00    25.86     9.25   186.10    16.07 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00\n" +
                    "    CLK  20180316/2215      8.00    40.68  -111.57  2529.00    32.39     9.20   157.40    16.19 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00\n" +
                    "    CLK  20180316/2230      8.00    40.68  -111.57  2529.00    31.84    11.29   147.10    16.70 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00\n" +
                    "    CRN  20180316/2225      8.00    38.29  -111.26  1676.00    54.16     5.30   320.90    14.67 -9999.00 -9999.00    23.26    29.81 -9999.00 -9999.00\n" +
                    "    CRN  20180316/2230      8.00    38.29  -111.26  1676.00    54.89     5.41   169.30    14.70 -9999.00 -9999.00    23.73    29.60 -9999.00 -9999.00\n" +
                    "    CRN  20180316/2235      8.00    38.29  -111.26  1676.00    53.98    11.58   127.00    14.86 -9999.00 -9999.00    23.48    30.28 -9999.00 -9999.00\n" +
                    "    CRN  20180316/2240      8.00    38.29  -111.26  1676.00    54.65    13.68   127.50    14.36 -9999.00 -9999.00    23.06    29.03 -9999.00 -9999.00\n" +
                    "    PRP  20180316/2230      8.00    41.26  -112.44  2004.00    36.63     6.17   141.20    13.07 -9999.00 -9999.00    31.99    83.10 -9999.00 -9999.00\n" +
                    "    SB2  20180316/2245      8.00    41.19  -111.87  2805.00    24.94     5.29   197.50     9.78 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00 -9999.00");

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
            client.putHandler();
        } finally {
            client.shutdown();
        }
    }
}
