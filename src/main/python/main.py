import generated.service_pb2 as pb2
from grpc import server
from grpc import StatusCode
import generated.service_pb2_grpc as grpc
from cipher import *
import database
from concurrent import futures

class Service(grpc.ScravaServicer):

    def startConnection(self, request: pb2.TokenMessage, context) -> pb2.CipherUpdate:
        print("Start connection with client")
        print(request.uid)
        print(len(request.token))
        if(len(request.token) != 0 and not database.check_token(request.token, request.uid)):
            context.set_code(StatusCode.NOT_FOUND)
            context.set_details("Invalid or expired token")
        return pb2.CipherUpdate(publicKey=RSA.get_public_key())
    
    def refreshCypher(self, _: pb2.EmptyMessage, context) -> pb2.CipherUpdate:
        print("Refresh cipher")
        return pb2.CipherUpdate(publicKey=RSA.get_public_key())

    def login(self, request: pb2.ClientLogin, context) -> pb2.ClientData:
        print("Login")
        if(request.pk != RSA.get_public_key()):
            context.set_code(StatusCode.FAILED_PRECONDITION)
            context.set_details("Expired key")
            return pb2.ClientData()
        user = database.check_user(request.name, RSA.decode(request.password))
        if user.id != -1:
            print("success")
            return user
        else:
            context.set_details("Wrong credentials")
            context.set_code(StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()

    def register(self, request: pb2.ClientRegister, context) -> pb2.ClientData:
        print("Register")
        if(request.pk != RSA.get_public_key()):
            context.set_code(StatusCode.FAILED_PRECONDITION)
            context.set_details("Expired key")
            return pb2.ClientData()
        pw = RSA.decode(request.password)
        if len(pw) < 8 or len(pw) > 256:
            context.set_details(f"Wrong password length ({len(pw)}); Password must be between 8and 256 characters")
            context.set_code(StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()
        if not database.create_user(request.name, SHA_256.encode(pw)):
            context.set_details("Name already in use")
            context.set_code(StatusCode.INVALID_ARGUMENT)
            return pb2.ClientData()
        return database.check_user(request.name, pw)

    def deleteToken(self, request: pb2.TokenMessage, context):
        print("Delete token")
        if(not database.delete_token(request.uid, request.token)):
            context.set_details("Invalid credentials")
            context.set_code(StatusCode.INVALID_ARGUMENT)
        return pb2.EmptyMessage()
    
    def deleteProject(self, request: pb2.AuthoredObject, context):
        if(database.check_token(request.token, request.uid)):
            database.delete_project(request.uid, request.obj.id)
        else:
            context.set_details("Invalid credentials")
            context.set_code(StatusCode.PERMISSION_DENIED)
        return pb2.EmptyMessage()

    def saveProject(self, request: pb2.AuthoredObject, context) -> pb2.ObjectDescriptor:
        if(database.check_token(request.token, request.uid)):
            database.save_project(request.obj.id, request.uid, request.obj.name, request.obj.obj)
            return pb2.ObjectDescriptor(id=database.get_last_id(), name=request.obj.name)
        else:
            context.set_details("Invalid credentials")
            context.set_code(StatusCode.PERMISSION_DENIED)
            return pb2.ObjectDescriptor()

    def getTutorialList(self, request, context):
        pass

    def getProjectList(self, request: pb2.Query, context) -> pb2.ObjectDescriptor:
        res = database.search_projects(request.offset, request.query)
        for element in res:
            yield pb2.ObjectDescriptor(id=element[0], name = element[1])


    def getTutorial(self, request, context):
        pass

    def getProject(self, request: pb2.Query, context):
        return database.get_project(int(request.query))

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