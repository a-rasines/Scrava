import generated.service_pb2 as pb2
import generated.service_pb2_grpc as grpc

class Service(grpc.ScravaServicer):

    def startConnection(self, _: pb2.EmptyMessage, context) -> pb2.ClientData:
        pass

    def login(self, request: pb2.ClientLogin, context) -> pb2.ClientData:
        pass

    def register(self, request: pb2.ClientRegister, context) -> pb2.ClientData:
        pass

    def saveProject(self, request: pb2.SerializedObject, context) -> pb2.EmptyMessage:
        pass

    def getTutorialList(self, request, context):
        pass

    def getProjectList(self, request, context):
        pass

    def getTutorial(self, request, context):
        pass

    def getProject(self, request, context):
        pass