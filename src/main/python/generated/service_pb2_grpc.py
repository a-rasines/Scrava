# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc
import warnings

import generated.service_pb2 as service__pb2

GRPC_GENERATED_VERSION = '1.64.0'
GRPC_VERSION = grpc.__version__
EXPECTED_ERROR_RELEASE = '1.65.0'
SCHEDULED_RELEASE_DATE = 'June 25, 2024'
_version_not_supported = False

try:
    from grpc._utilities import first_version_is_lower
    _version_not_supported = first_version_is_lower(GRPC_VERSION, GRPC_GENERATED_VERSION)
except ImportError:
    _version_not_supported = True

if _version_not_supported:
    warnings.warn(
        f'The grpc package installed is at version {GRPC_VERSION},'
        + f' but the generated code in service_pb2_grpc.py depends on'
        + f' grpcio>={GRPC_GENERATED_VERSION}.'
        + f' Please upgrade your grpc module to grpcio>={GRPC_GENERATED_VERSION}'
        + f' or downgrade your generated code using grpcio-tools<={GRPC_VERSION}.'
        + f' This warning will become an error in {EXPECTED_ERROR_RELEASE},'
        + f' scheduled for release on {SCHEDULED_RELEASE_DATE}.',
        RuntimeWarning
    )


class ScravaStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.startConnection = channel.unary_unary(
                '/Scrava/startConnection',
                request_serializer=service__pb2.TokenMessage.SerializeToString,
                response_deserializer=service__pb2.CipherUpdate.FromString,
                _registered_method=True)
        self.login = channel.unary_unary(
                '/Scrava/login',
                request_serializer=service__pb2.ClientLogin.SerializeToString,
                response_deserializer=service__pb2.ClientData.FromString,
                _registered_method=True)
        self.register = channel.unary_unary(
                '/Scrava/register',
                request_serializer=service__pb2.ClientRegister.SerializeToString,
                response_deserializer=service__pb2.ClientData.FromString,
                _registered_method=True)
        self.deleteToken = channel.unary_unary(
                '/Scrava/deleteToken',
                request_serializer=service__pb2.TokenMessage.SerializeToString,
                response_deserializer=service__pb2.EmptyMessage.FromString,
                _registered_method=True)
        self.saveProject = channel.unary_unary(
                '/Scrava/saveProject',
                request_serializer=service__pb2.AuthoredObject.SerializeToString,
                response_deserializer=service__pb2.ObjectDescriptor.FromString,
                _registered_method=True)
        self.saveTutorial = channel.unary_unary(
                '/Scrava/saveTutorial',
                request_serializer=service__pb2.AuthoredObject.SerializeToString,
                response_deserializer=service__pb2.EmptyMessage.FromString,
                _registered_method=True)
        self.deleteProject = channel.unary_unary(
                '/Scrava/deleteProject',
                request_serializer=service__pb2.AuthoredObject.SerializeToString,
                response_deserializer=service__pb2.EmptyMessage.FromString,
                _registered_method=True)
        self.getTutorialList = channel.unary_stream(
                '/Scrava/getTutorialList',
                request_serializer=service__pb2.Query.SerializeToString,
                response_deserializer=service__pb2.ObjectDescriptor.FromString,
                _registered_method=True)
        self.getProjectList = channel.unary_stream(
                '/Scrava/getProjectList',
                request_serializer=service__pb2.Query.SerializeToString,
                response_deserializer=service__pb2.ObjectDescriptor.FromString,
                _registered_method=True)
        self.refreshCypher = channel.unary_unary(
                '/Scrava/refreshCypher',
                request_serializer=service__pb2.EmptyMessage.SerializeToString,
                response_deserializer=service__pb2.CipherUpdate.FromString,
                _registered_method=True)
        self.getTutorial = channel.unary_unary(
                '/Scrava/getTutorial',
                request_serializer=service__pb2.Query.SerializeToString,
                response_deserializer=service__pb2.SerializedObject.FromString,
                _registered_method=True)
        self.getProject = channel.unary_unary(
                '/Scrava/getProject',
                request_serializer=service__pb2.Query.SerializeToString,
                response_deserializer=service__pb2.SerializedObject.FromString,
                _registered_method=True)


class ScravaServicer(object):
    """Missing associated documentation comment in .proto file."""

    def startConnection(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def login(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def register(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def deleteToken(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def saveProject(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def saveTutorial(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def deleteProject(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def getTutorialList(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def getProjectList(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def refreshCypher(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def getTutorial(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def getProject(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_ScravaServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'startConnection': grpc.unary_unary_rpc_method_handler(
                    servicer.startConnection,
                    request_deserializer=service__pb2.TokenMessage.FromString,
                    response_serializer=service__pb2.CipherUpdate.SerializeToString,
            ),
            'login': grpc.unary_unary_rpc_method_handler(
                    servicer.login,
                    request_deserializer=service__pb2.ClientLogin.FromString,
                    response_serializer=service__pb2.ClientData.SerializeToString,
            ),
            'register': grpc.unary_unary_rpc_method_handler(
                    servicer.register,
                    request_deserializer=service__pb2.ClientRegister.FromString,
                    response_serializer=service__pb2.ClientData.SerializeToString,
            ),
            'deleteToken': grpc.unary_unary_rpc_method_handler(
                    servicer.deleteToken,
                    request_deserializer=service__pb2.TokenMessage.FromString,
                    response_serializer=service__pb2.EmptyMessage.SerializeToString,
            ),
            'saveProject': grpc.unary_unary_rpc_method_handler(
                    servicer.saveProject,
                    request_deserializer=service__pb2.AuthoredObject.FromString,
                    response_serializer=service__pb2.ObjectDescriptor.SerializeToString,
            ),
            'saveTutorial': grpc.unary_unary_rpc_method_handler(
                    servicer.saveTutorial,
                    request_deserializer=service__pb2.AuthoredObject.FromString,
                    response_serializer=service__pb2.EmptyMessage.SerializeToString,
            ),
            'deleteProject': grpc.unary_unary_rpc_method_handler(
                    servicer.deleteProject,
                    request_deserializer=service__pb2.AuthoredObject.FromString,
                    response_serializer=service__pb2.EmptyMessage.SerializeToString,
            ),
            'getTutorialList': grpc.unary_stream_rpc_method_handler(
                    servicer.getTutorialList,
                    request_deserializer=service__pb2.Query.FromString,
                    response_serializer=service__pb2.ObjectDescriptor.SerializeToString,
            ),
            'getProjectList': grpc.unary_stream_rpc_method_handler(
                    servicer.getProjectList,
                    request_deserializer=service__pb2.Query.FromString,
                    response_serializer=service__pb2.ObjectDescriptor.SerializeToString,
            ),
            'refreshCypher': grpc.unary_unary_rpc_method_handler(
                    servicer.refreshCypher,
                    request_deserializer=service__pb2.EmptyMessage.FromString,
                    response_serializer=service__pb2.CipherUpdate.SerializeToString,
            ),
            'getTutorial': grpc.unary_unary_rpc_method_handler(
                    servicer.getTutorial,
                    request_deserializer=service__pb2.Query.FromString,
                    response_serializer=service__pb2.SerializedObject.SerializeToString,
            ),
            'getProject': grpc.unary_unary_rpc_method_handler(
                    servicer.getProject,
                    request_deserializer=service__pb2.Query.FromString,
                    response_serializer=service__pb2.SerializedObject.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'Scrava', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))
    server.add_registered_method_handlers('Scrava', rpc_method_handlers)


 # This class is part of an EXPERIMENTAL API.
class Scrava(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def startConnection(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/startConnection',
            service__pb2.TokenMessage.SerializeToString,
            service__pb2.CipherUpdate.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def login(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/login',
            service__pb2.ClientLogin.SerializeToString,
            service__pb2.ClientData.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def register(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/register',
            service__pb2.ClientRegister.SerializeToString,
            service__pb2.ClientData.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def deleteToken(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/deleteToken',
            service__pb2.TokenMessage.SerializeToString,
            service__pb2.EmptyMessage.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def saveProject(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/saveProject',
            service__pb2.AuthoredObject.SerializeToString,
            service__pb2.ObjectDescriptor.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def saveTutorial(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/saveTutorial',
            service__pb2.AuthoredObject.SerializeToString,
            service__pb2.EmptyMessage.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def deleteProject(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/deleteProject',
            service__pb2.AuthoredObject.SerializeToString,
            service__pb2.EmptyMessage.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def getTutorialList(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_stream(
            request,
            target,
            '/Scrava/getTutorialList',
            service__pb2.Query.SerializeToString,
            service__pb2.ObjectDescriptor.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def getProjectList(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_stream(
            request,
            target,
            '/Scrava/getProjectList',
            service__pb2.Query.SerializeToString,
            service__pb2.ObjectDescriptor.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def refreshCypher(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/refreshCypher',
            service__pb2.EmptyMessage.SerializeToString,
            service__pb2.CipherUpdate.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def getTutorial(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/getTutorial',
            service__pb2.Query.SerializeToString,
            service__pb2.SerializedObject.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def getProject(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/Scrava/getProject',
            service__pb2.Query.SerializeToString,
            service__pb2.SerializedObject.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)
