package io.grpc.election;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.0)",
    comments = "Source: election.proto")
public final class ElectionServiceGrpc {

  private ElectionServiceGrpc() {}

  public static final String SERVICE_NAME = "ElectionService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSendHeartbeatMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> METHOD_SEND_HEARTBEAT = getSendHeartbeatMethodHelper();

  private static volatile io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getSendHeartbeatMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getSendHeartbeatMethod() {
    return getSendHeartbeatMethodHelper();
  }

  private static io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getSendHeartbeatMethodHelper() {
    io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg, io.grpc.election.ElectionReply> getSendHeartbeatMethod;
    if ((getSendHeartbeatMethod = ElectionServiceGrpc.getSendHeartbeatMethod) == null) {
      synchronized (ElectionServiceGrpc.class) {
        if ((getSendHeartbeatMethod = ElectionServiceGrpc.getSendHeartbeatMethod) == null) {
          ElectionServiceGrpc.getSendHeartbeatMethod = getSendHeartbeatMethod = 
              io.grpc.MethodDescriptor.<io.grpc.election.ElectionMsg, io.grpc.election.ElectionReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ElectionService", "sendHeartbeat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.election.ElectionMsg.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.election.ElectionReply.getDefaultInstance()))
                  .setSchemaDescriptor(new ElectionServiceMethodDescriptorSupplier("sendHeartbeat"))
                  .build();
          }
        }
     }
     return getSendHeartbeatMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRunElectionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> METHOD_RUN_ELECTION = getRunElectionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getRunElectionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getRunElectionMethod() {
    return getRunElectionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg,
      io.grpc.election.ElectionReply> getRunElectionMethodHelper() {
    io.grpc.MethodDescriptor<io.grpc.election.ElectionMsg, io.grpc.election.ElectionReply> getRunElectionMethod;
    if ((getRunElectionMethod = ElectionServiceGrpc.getRunElectionMethod) == null) {
      synchronized (ElectionServiceGrpc.class) {
        if ((getRunElectionMethod = ElectionServiceGrpc.getRunElectionMethod) == null) {
          ElectionServiceGrpc.getRunElectionMethod = getRunElectionMethod = 
              io.grpc.MethodDescriptor.<io.grpc.election.ElectionMsg, io.grpc.election.ElectionReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ElectionService", "runElection"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.election.ElectionMsg.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.grpc.election.ElectionReply.getDefaultInstance()))
                  .setSchemaDescriptor(new ElectionServiceMethodDescriptorSupplier("runElection"))
                  .build();
          }
        }
     }
     return getRunElectionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ElectionServiceStub newStub(io.grpc.Channel channel) {
    return new ElectionServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ElectionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ElectionServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ElectionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ElectionServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ElectionServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendHeartbeat(io.grpc.election.ElectionMsg request,
        io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSendHeartbeatMethodHelper(), responseObserver);
    }

    /**
     */
    public void runElection(io.grpc.election.ElectionMsg request,
        io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply> responseObserver) {
      asyncUnimplementedUnaryCall(getRunElectionMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendHeartbeatMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                io.grpc.election.ElectionMsg,
                io.grpc.election.ElectionReply>(
                  this, METHODID_SEND_HEARTBEAT)))
          .addMethod(
            getRunElectionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                io.grpc.election.ElectionMsg,
                io.grpc.election.ElectionReply>(
                  this, METHODID_RUN_ELECTION)))
          .build();
    }
  }

  /**
   */
  public static final class ElectionServiceStub extends io.grpc.stub.AbstractStub<ElectionServiceStub> {
    private ElectionServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceStub(channel, callOptions);
    }

    /**
     */
    public void sendHeartbeat(io.grpc.election.ElectionMsg request,
        io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendHeartbeatMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void runElection(io.grpc.election.ElectionMsg request,
        io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRunElectionMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ElectionServiceBlockingStub extends io.grpc.stub.AbstractStub<ElectionServiceBlockingStub> {
    private ElectionServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.election.ElectionReply sendHeartbeat(io.grpc.election.ElectionMsg request) {
      return blockingUnaryCall(
          getChannel(), getSendHeartbeatMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public io.grpc.election.ElectionReply runElection(io.grpc.election.ElectionMsg request) {
      return blockingUnaryCall(
          getChannel(), getRunElectionMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ElectionServiceFutureStub extends io.grpc.stub.AbstractStub<ElectionServiceFutureStub> {
    private ElectionServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ElectionServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ElectionServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ElectionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.election.ElectionReply> sendHeartbeat(
        io.grpc.election.ElectionMsg request) {
      return futureUnaryCall(
          getChannel().newCall(getSendHeartbeatMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.election.ElectionReply> runElection(
        io.grpc.election.ElectionMsg request) {
      return futureUnaryCall(
          getChannel().newCall(getRunElectionMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_HEARTBEAT = 0;
  private static final int METHODID_RUN_ELECTION = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ElectionServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ElectionServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_HEARTBEAT:
          serviceImpl.sendHeartbeat((io.grpc.election.ElectionMsg) request,
              (io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply>) responseObserver);
          break;
        case METHODID_RUN_ELECTION:
          serviceImpl.runElection((io.grpc.election.ElectionMsg) request,
              (io.grpc.stub.StreamObserver<io.grpc.election.ElectionReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ElectionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ElectionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.grpc.election.Elect.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ElectionService");
    }
  }

  private static final class ElectionServiceFileDescriptorSupplier
      extends ElectionServiceBaseDescriptorSupplier {
    ElectionServiceFileDescriptorSupplier() {}
  }

  private static final class ElectionServiceMethodDescriptorSupplier
      extends ElectionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ElectionServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ElectionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ElectionServiceFileDescriptorSupplier())
              .addMethod(getSendHeartbeatMethodHelper())
              .addMethod(getRunElectionMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
