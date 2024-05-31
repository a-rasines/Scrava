import generated.service_pb2 as pb2
from grpc import server
import generated.service_pb2_grpc as grpc
from cipher import *
import database
from concurrent import futures

class Service(grpc.ScravaServicer):

    def startConnection(self, _: pb2.EmptyMessage, context) -> pb2.CipherUpdate:
        print("Start connection with client")
        return pb2.CipherUpdate(publicKey=RSA.get_public_key());
    
    def refreshCipher(self, _: pb2.EmptyMessage, context) -> pb2.CipherUpdate:
        print("Refresh cipher")
        return pb2.CipherUpdate(publicKey=RSA.get_public_key());

    def login(self, request: pb2.ClientLogin, context) -> pb2.ClientData:
        print("Login")
        user = database.check_user(request.name, RSA.decode(request.password))
        if user.id != -1:
            return user
        else:
            context.set_details("Wrong credentials")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()

    def register(self, request: pb2.ClientRegister, context) -> pb2.ClientData:
        print("Register")
        pw = RSA.decode(request.password)
        if len(pw) < 8 or len(pw) > 256:
            context.set_details(f"Wrong password length ({len(pw)}); Password must be between 8and 256 characters")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()
        if not database.create_user(request.name, SHA_256.encode(pw)):
            context.set_details("Name already in use")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()
        return database.check_user(request.name, pw)

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

#https://grpc.io/docs/languages/python/basics/
def serve():
    srver = server(futures.ThreadPoolExecutor(max_workers=10))
    grpc.add_ScravaServicer_to_server(Service(), srver)
    srver.add_insecure_port("[::]:8080")
    srver.start()
    print("Server online")
    srver.wait_for_termination()

if __name__ == "__main__":
    serve()