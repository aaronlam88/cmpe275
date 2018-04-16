package com.cmpe275.grpcComm;

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
 * <pre>
 * grpc api function
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.0)",
    comments = "Source: comm.proto")
public final class CommunicationServiceGrpc {

  private CommunicationServiceGrpc() {}

  public static final String SERVICE_NAME = "grpcComm.CommunicationService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPutHandlerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> METHOD_PUT_HANDLER = getPutHandlerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPutHandlerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPutHandlerMethod() {
    return getPutHandlerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPutHandlerMethodHelper() {
    io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response> getPutHandlerMethod;
    if ((getPutHandlerMethod = CommunicationServiceGrpc.getPutHandlerMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getPutHandlerMethod = CommunicationServiceGrpc.getPutHandlerMethod) == null) {
          CommunicationServiceGrpc.getPutHandlerMethod = getPutHandlerMethod = 
              io.grpc.MethodDescriptor.<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "grpcComm.CommunicationService", "putHandler"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("putHandler"))
                  .build();
          }
        }
     }
     return getPutHandlerMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetHandlerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> METHOD_GET_HANDLER = getGetHandlerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getGetHandlerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getGetHandlerMethod() {
    return getGetHandlerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getGetHandlerMethodHelper() {
    io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response> getGetHandlerMethod;
    if ((getGetHandlerMethod = CommunicationServiceGrpc.getGetHandlerMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getGetHandlerMethod = CommunicationServiceGrpc.getGetHandlerMethod) == null) {
          CommunicationServiceGrpc.getGetHandlerMethod = getGetHandlerMethod = 
              io.grpc.MethodDescriptor.<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "grpcComm.CommunicationService", "getHandler"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("getHandler"))
                  .build();
          }
        }
     }
     return getGetHandlerMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPingMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> METHOD_PING = getPingMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPingMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPingMethod() {
    return getPingMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request,
      com.cmpe275.grpcComm.Response> getPingMethodHelper() {
    io.grpc.MethodDescriptor<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response> getPingMethod;
    if ((getPingMethod = CommunicationServiceGrpc.getPingMethod) == null) {
      synchronized (CommunicationServiceGrpc.class) {
        if ((getPingMethod = CommunicationServiceGrpc.getPingMethod) == null) {
          CommunicationServiceGrpc.getPingMethod = getPingMethod = 
              io.grpc.MethodDescriptor.<com.cmpe275.grpcComm.Request, com.cmpe275.grpcComm.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "grpcComm.CommunicationService", "ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.cmpe275.grpcComm.Response.getDefaultInstance()))
                  .setSchemaDescriptor(new CommunicationServiceMethodDescriptorSupplier("ping"))
                  .build();
          }
        }
     }
     return getPingMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CommunicationServiceStub newStub(io.grpc.Channel channel) {
    return new CommunicationServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CommunicationServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CommunicationServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CommunicationServiceFutureStub(channel);
  }

  /**
   * <pre>
   * grpc api function
   * </pre>
   */
  public static abstract class CommunicationServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Request> putHandler(
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      return asyncUnimplementedStreamingCall(getPutHandlerMethodHelper(), responseObserver);
    }

    /**
     */
    public void getHandler(com.cmpe275.grpcComm.Request request,
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getGetHandlerMethodHelper(), responseObserver);
    }

    /**
     */
    public void ping(com.cmpe275.grpcComm.Request request,
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPutHandlerMethodHelper(),
            asyncClientStreamingCall(
              new MethodHandlers<
                com.cmpe275.grpcComm.Request,
                com.cmpe275.grpcComm.Response>(
                  this, METHODID_PUT_HANDLER)))
          .addMethod(
            getGetHandlerMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.cmpe275.grpcComm.Request,
                com.cmpe275.grpcComm.Response>(
                  this, METHODID_GET_HANDLER)))
          .addMethod(
            getPingMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.cmpe275.grpcComm.Request,
                com.cmpe275.grpcComm.Response>(
                  this, METHODID_PING)))
          .build();
    }
  }

  /**
   * <pre>
   * grpc api function
   * </pre>
   */
  public static final class CommunicationServiceStub extends io.grpc.stub.AbstractStub<CommunicationServiceStub> {
    private CommunicationServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Request> putHandler(
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getPutHandlerMethodHelper(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void getHandler(com.cmpe275.grpcComm.Request request,
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetHandlerMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ping(com.cmpe275.grpcComm.Request request,
        io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * grpc api function
   * </pre>
   */
  public static final class CommunicationServiceBlockingStub extends io.grpc.stub.AbstractStub<CommunicationServiceBlockingStub> {
    private CommunicationServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.cmpe275.grpcComm.Response> getHandler(
        com.cmpe275.grpcComm.Request request) {
      return blockingServerStreamingCall(
          getChannel(), getGetHandlerMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.cmpe275.grpcComm.Response ping(com.cmpe275.grpcComm.Request request) {
      return blockingUnaryCall(
          getChannel(), getPingMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * grpc api function
   * </pre>
   */
  public static final class CommunicationServiceFutureStub extends io.grpc.stub.AbstractStub<CommunicationServiceFutureStub> {
    private CommunicationServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CommunicationServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CommunicationServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CommunicationServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.cmpe275.grpcComm.Response> ping(
        com.cmpe275.grpcComm.Request request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_HANDLER = 0;
  private static final int METHODID_PING = 1;
  private static final int METHODID_PUT_HANDLER = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CommunicationServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CommunicationServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_HANDLER:
          serviceImpl.getHandler((com.cmpe275.grpcComm.Request) request,
              (io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response>) responseObserver);
          break;
        case METHODID_PING:
          serviceImpl.ping((com.cmpe275.grpcComm.Request) request,
              (io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response>) responseObserver);
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
        case METHODID_PUT_HANDLER:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.putHandler(
              (io.grpc.stub.StreamObserver<com.cmpe275.grpcComm.Response>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CommunicationServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.cmpe275.grpcComm.DataProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CommunicationService");
    }
  }

  private static final class CommunicationServiceFileDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier {
    CommunicationServiceFileDescriptorSupplier() {}
  }

  private static final class CommunicationServiceMethodDescriptorSupplier
      extends CommunicationServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CommunicationServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (CommunicationServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CommunicationServiceFileDescriptorSupplier())
              .addMethod(getPutHandlerMethodHelper())
              .addMethod(getGetHandlerMethodHelper())
              .addMethod(getPingMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
