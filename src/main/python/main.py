import generated.service_pb2 as pb2
import generated.service_pb2_grpc as grpc

class Service(grpc.ScravaServicer):

    def startConnection(self, request, context) -> pb2.ClientData:
        pass

    def login(self, request, context):
        pass

    def register(self, request, context):
        pass

    def saveProject(self, request, context):
        pass

    def getTutorialList(self, request, context):
        pass

    def getProjectList(self, request, context):
        pass

    def getTutorial(self, request, context):
        pass

    def getProject(self, request, context):
        pass