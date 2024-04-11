package server;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: .proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ScrabaGrpc {

  private ScrabaGrpc() {}

  public static final java.lang.String SERVICE_NAME = "Scraba";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.EmptyMessage,
      server.CipherServiceProto.ClientData> getStartConnectionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "startConnection",
      requestType = server.CipherServiceProto.EmptyMessage.class,
      responseType = server.CipherServiceProto.ClientData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.EmptyMessage,
      server.CipherServiceProto.ClientData> getStartConnectionMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.EmptyMessage, server.CipherServiceProto.ClientData> getStartConnectionMethod;
    if ((getStartConnectionMethod = ScrabaGrpc.getStartConnectionMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getStartConnectionMethod = ScrabaGrpc.getStartConnectionMethod) == null) {
          ScrabaGrpc.getStartConnectionMethod = getStartConnectionMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.EmptyMessage, server.CipherServiceProto.ClientData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "startConnection"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.EmptyMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.ClientData.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("startConnection"))
              .build();
        }
      }
    }
    return getStartConnectionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.ClientLogin,
      server.CipherServiceProto.ClientData> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "login",
      requestType = server.CipherServiceProto.ClientLogin.class,
      responseType = server.CipherServiceProto.ClientData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.ClientLogin,
      server.CipherServiceProto.ClientData> getLoginMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.ClientLogin, server.CipherServiceProto.ClientData> getLoginMethod;
    if ((getLoginMethod = ScrabaGrpc.getLoginMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getLoginMethod = ScrabaGrpc.getLoginMethod) == null) {
          ScrabaGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.ClientLogin, server.CipherServiceProto.ClientData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.ClientLogin.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.ClientData.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.ClientRegister,
      server.CipherServiceProto.ClientData> getRegisterMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "register",
      requestType = server.CipherServiceProto.ClientRegister.class,
      responseType = server.CipherServiceProto.ClientData.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.ClientRegister,
      server.CipherServiceProto.ClientData> getRegisterMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.ClientRegister, server.CipherServiceProto.ClientData> getRegisterMethod;
    if ((getRegisterMethod = ScrabaGrpc.getRegisterMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getRegisterMethod = ScrabaGrpc.getRegisterMethod) == null) {
          ScrabaGrpc.getRegisterMethod = getRegisterMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.ClientRegister, server.CipherServiceProto.ClientData>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "register"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.ClientRegister.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.ClientData.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("register"))
              .build();
        }
      }
    }
    return getRegisterMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.SerializedObject,
      server.CipherServiceProto.EmptyMessage> getSaveProjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "saveProject",
      requestType = server.CipherServiceProto.SerializedObject.class,
      responseType = server.CipherServiceProto.EmptyMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.SerializedObject,
      server.CipherServiceProto.EmptyMessage> getSaveProjectMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.SerializedObject, server.CipherServiceProto.EmptyMessage> getSaveProjectMethod;
    if ((getSaveProjectMethod = ScrabaGrpc.getSaveProjectMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getSaveProjectMethod = ScrabaGrpc.getSaveProjectMethod) == null) {
          ScrabaGrpc.getSaveProjectMethod = getSaveProjectMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.SerializedObject, server.CipherServiceProto.EmptyMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "saveProject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.SerializedObject.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.EmptyMessage.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("saveProject"))
              .build();
        }
      }
    }
    return getSaveProjectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.StringList> getGetTutorialListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getTutorialList",
      requestType = server.CipherServiceProto.Query.class,
      responseType = server.CipherServiceProto.StringList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.StringList> getGetTutorialListMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.Query, server.CipherServiceProto.StringList> getGetTutorialListMethod;
    if ((getGetTutorialListMethod = ScrabaGrpc.getGetTutorialListMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getGetTutorialListMethod = ScrabaGrpc.getGetTutorialListMethod) == null) {
          ScrabaGrpc.getGetTutorialListMethod = getGetTutorialListMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.Query, server.CipherServiceProto.StringList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTutorialList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.Query.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.StringList.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("getTutorialList"))
              .build();
        }
      }
    }
    return getGetTutorialListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.StringList> getGetProjectListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getProjectList",
      requestType = server.CipherServiceProto.Query.class,
      responseType = server.CipherServiceProto.StringList.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.StringList> getGetProjectListMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.Query, server.CipherServiceProto.StringList> getGetProjectListMethod;
    if ((getGetProjectListMethod = ScrabaGrpc.getGetProjectListMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getGetProjectListMethod = ScrabaGrpc.getGetProjectListMethod) == null) {
          ScrabaGrpc.getGetProjectListMethod = getGetProjectListMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.Query, server.CipherServiceProto.StringList>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getProjectList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.Query.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.StringList.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("getProjectList"))
              .build();
        }
      }
    }
    return getGetProjectListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.SerializedObject> getGetTutorialMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getTutorial",
      requestType = server.CipherServiceProto.Query.class,
      responseType = server.CipherServiceProto.SerializedObject.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.SerializedObject> getGetTutorialMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.Query, server.CipherServiceProto.SerializedObject> getGetTutorialMethod;
    if ((getGetTutorialMethod = ScrabaGrpc.getGetTutorialMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getGetTutorialMethod = ScrabaGrpc.getGetTutorialMethod) == null) {
          ScrabaGrpc.getGetTutorialMethod = getGetTutorialMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.Query, server.CipherServiceProto.SerializedObject>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTutorial"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.Query.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.SerializedObject.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("getTutorial"))
              .build();
        }
      }
    }
    return getGetTutorialMethod;
  }

  private static volatile io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.SerializedObject> getGetProjectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getProject",
      requestType = server.CipherServiceProto.Query.class,
      responseType = server.CipherServiceProto.SerializedObject.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<server.CipherServiceProto.Query,
      server.CipherServiceProto.SerializedObject> getGetProjectMethod() {
    io.grpc.MethodDescriptor<server.CipherServiceProto.Query, server.CipherServiceProto.SerializedObject> getGetProjectMethod;
    if ((getGetProjectMethod = ScrabaGrpc.getGetProjectMethod) == null) {
      synchronized (ScrabaGrpc.class) {
        if ((getGetProjectMethod = ScrabaGrpc.getGetProjectMethod) == null) {
          ScrabaGrpc.getGetProjectMethod = getGetProjectMethod =
              io.grpc.MethodDescriptor.<server.CipherServiceProto.Query, server.CipherServiceProto.SerializedObject>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getProject"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.Query.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  server.CipherServiceProto.SerializedObject.getDefaultInstance()))
              .setSchemaDescriptor(new ScrabaMethodDescriptorSupplier("getProject"))
              .build();
        }
      }
    }
    return getGetProjectMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ScrabaStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ScrabaStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ScrabaStub>() {
        @java.lang.Override
        public ScrabaStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ScrabaStub(channel, callOptions);
        }
      };
    return ScrabaStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ScrabaBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ScrabaBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ScrabaBlockingStub>() {
        @java.lang.Override
        public ScrabaBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ScrabaBlockingStub(channel, callOptions);
        }
      };
    return ScrabaBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ScrabaFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ScrabaFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ScrabaFutureStub>() {
        @java.lang.Override
        public ScrabaFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ScrabaFutureStub(channel, callOptions);
        }
      };
    return ScrabaFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void startConnection(server.CipherServiceProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartConnectionMethod(), responseObserver);
    }

    /**
     */
    default void login(server.CipherServiceProto.ClientLogin request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     */
    default void register(server.CipherServiceProto.ClientRegister request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterMethod(), responseObserver);
    }

    /**
     */
    default void saveProject(server.CipherServiceProto.SerializedObject request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.EmptyMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSaveProjectMethod(), responseObserver);
    }

    /**
     */
    default void getTutorialList(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTutorialListMethod(), responseObserver);
    }

    /**
     */
    default void getProjectList(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProjectListMethod(), responseObserver);
    }

    /**
     */
    default void getTutorial(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTutorialMethod(), responseObserver);
    }

    /**
     */
    default void getProject(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetProjectMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Scraba.
   */
  public static abstract class ScrabaImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ScrabaGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Scraba.
   */
  public static final class ScrabaStub
      extends io.grpc.stub.AbstractAsyncStub<ScrabaStub> {
    private ScrabaStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ScrabaStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ScrabaStub(channel, callOptions);
    }

    /**
     */
    public void startConnection(server.CipherServiceProto.EmptyMessage request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartConnectionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void login(server.CipherServiceProto.ClientLogin request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void register(server.CipherServiceProto.ClientRegister request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void saveProject(server.CipherServiceProto.SerializedObject request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.EmptyMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSaveProjectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTutorialList(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTutorialListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getProjectList(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProjectListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTutorial(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTutorialMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getProject(server.CipherServiceProto.Query request,
        io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetProjectMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Scraba.
   */
  public static final class ScrabaBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ScrabaBlockingStub> {
    private ScrabaBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ScrabaBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ScrabaBlockingStub(channel, callOptions);
    }

    /**
     */
    public server.CipherServiceProto.ClientData startConnection(server.CipherServiceProto.EmptyMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartConnectionMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.ClientData login(server.CipherServiceProto.ClientLogin request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.ClientData register(server.CipherServiceProto.ClientRegister request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.EmptyMessage saveProject(server.CipherServiceProto.SerializedObject request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSaveProjectMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.StringList getTutorialList(server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTutorialListMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.StringList getProjectList(server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProjectListMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.SerializedObject getTutorial(server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTutorialMethod(), getCallOptions(), request);
    }

    /**
     */
    public server.CipherServiceProto.SerializedObject getProject(server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetProjectMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Scraba.
   */
  public static final class ScrabaFutureStub
      extends io.grpc.stub.AbstractFutureStub<ScrabaFutureStub> {
    private ScrabaFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ScrabaFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ScrabaFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.ClientData> startConnection(
        server.CipherServiceProto.EmptyMessage request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartConnectionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.ClientData> login(
        server.CipherServiceProto.ClientLogin request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.ClientData> register(
        server.CipherServiceProto.ClientRegister request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.EmptyMessage> saveProject(
        server.CipherServiceProto.SerializedObject request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSaveProjectMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.StringList> getTutorialList(
        server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTutorialListMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.StringList> getProjectList(
        server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProjectListMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.SerializedObject> getTutorial(
        server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTutorialMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<server.CipherServiceProto.SerializedObject> getProject(
        server.CipherServiceProto.Query request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetProjectMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START_CONNECTION = 0;
  private static final int METHODID_LOGIN = 1;
  private static final int METHODID_REGISTER = 2;
  private static final int METHODID_SAVE_PROJECT = 3;
  private static final int METHODID_GET_TUTORIAL_LIST = 4;
  private static final int METHODID_GET_PROJECT_LIST = 5;
  private static final int METHODID_GET_TUTORIAL = 6;
  private static final int METHODID_GET_PROJECT = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_CONNECTION:
          serviceImpl.startConnection((server.CipherServiceProto.EmptyMessage) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData>) responseObserver);
          break;
        case METHODID_LOGIN:
          serviceImpl.login((server.CipherServiceProto.ClientLogin) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData>) responseObserver);
          break;
        case METHODID_REGISTER:
          serviceImpl.register((server.CipherServiceProto.ClientRegister) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.ClientData>) responseObserver);
          break;
        case METHODID_SAVE_PROJECT:
          serviceImpl.saveProject((server.CipherServiceProto.SerializedObject) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.EmptyMessage>) responseObserver);
          break;
        case METHODID_GET_TUTORIAL_LIST:
          serviceImpl.getTutorialList((server.CipherServiceProto.Query) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList>) responseObserver);
          break;
        case METHODID_GET_PROJECT_LIST:
          serviceImpl.getProjectList((server.CipherServiceProto.Query) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.StringList>) responseObserver);
          break;
        case METHODID_GET_TUTORIAL:
          serviceImpl.getTutorial((server.CipherServiceProto.Query) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject>) responseObserver);
          break;
        case METHODID_GET_PROJECT:
          serviceImpl.getProject((server.CipherServiceProto.Query) request,
              (io.grpc.stub.StreamObserver<server.CipherServiceProto.SerializedObject>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getStartConnectionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.EmptyMessage,
              server.CipherServiceProto.ClientData>(
                service, METHODID_START_CONNECTION)))
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.ClientLogin,
              server.CipherServiceProto.ClientData>(
                service, METHODID_LOGIN)))
        .addMethod(
          getRegisterMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.ClientRegister,
              server.CipherServiceProto.ClientData>(
                service, METHODID_REGISTER)))
        .addMethod(
          getSaveProjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.SerializedObject,
              server.CipherServiceProto.EmptyMessage>(
                service, METHODID_SAVE_PROJECT)))
        .addMethod(
          getGetTutorialListMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.Query,
              server.CipherServiceProto.StringList>(
                service, METHODID_GET_TUTORIAL_LIST)))
        .addMethod(
          getGetProjectListMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.Query,
              server.CipherServiceProto.StringList>(
                service, METHODID_GET_PROJECT_LIST)))
        .addMethod(
          getGetTutorialMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.Query,
              server.CipherServiceProto.SerializedObject>(
                service, METHODID_GET_TUTORIAL)))
        .addMethod(
          getGetProjectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              server.CipherServiceProto.Query,
              server.CipherServiceProto.SerializedObject>(
                service, METHODID_GET_PROJECT)))
        .build();
  }

  private static abstract class ScrabaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ScrabaBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return server.CipherServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Scraba");
    }
  }

  private static final class ScrabaFileDescriptorSupplier
      extends ScrabaBaseDescriptorSupplier {
    ScrabaFileDescriptorSupplier() {}
  }

  private static final class ScrabaMethodDescriptorSupplier
      extends ScrabaBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ScrabaMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ScrabaGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ScrabaFileDescriptorSupplier())
              .addMethod(getStartConnectionMethod())
              .addMethod(getLoginMethod())
              .addMethod(getRegisterMethod())
              .addMethod(getSaveProjectMethod())
              .addMethod(getGetTutorialListMethod())
              .addMethod(getGetProjectListMethod())
              .addMethod(getGetTutorialMethod())
              .addMethod(getGetProjectMethod())
              .build();
        }
      }
    }
    return result;
  }
}
