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
import java.io.*;
import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectClient {
    private static final Logger logger = Logger.getLogger(ProjectClient.class.getName());

    private static final int fragmentSize = 1024000; // 1,024,000 char ~= 1MB

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
    public void putHandler(String filePath) {
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
//            String uuid = "uuid";
//            int numberOfFragment = 1;
//            int mediaType = 1;

            // build proto request

            // read file line by line
            File file = new File(filePath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // need to add back new line to ensure that lines are end with \n
                stringBuffer.append(line + "\n");
                // when size is >= fragment size, send it to server
                if (stringBuffer.length() >= fragmentSize) {
                    DatFragment datFragment = DatFragment
                            .newBuilder()
                            .setData(ByteString.copyFromUtf8(stringBuffer.toString()))
                            .build();

                    Request request = Request
                            .newBuilder()
                            .setPutRequest(
                                    PutRequest
                                            .newBuilder()
                                            .setDatFragment(datFragment)
                                            .build()
                            )
                            .build();

                    requestObserver.onNext(request);

                    stringBuffer = new StringBuffer(); // clean buffer
                }
            }
            bufferedReader.close();
            fileReader.close();

            // send completed
            requestObserver.onCompleted();
            done.await();
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            logger.log(Level.WARNING, "RPC failed: {0}", e.getMessage());
            return;
        } catch (FileNotFoundException e) {
            logger.info(e.getStackTrace().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("putHandler DONE");
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
//            client.ping();
//            client.getHandler();
            client.putHandler("/Users/aaronlam/Desktop/test_data/3.mesowest.out");
        } finally {
            client.shutdown();
        }
    }
}
