package remote;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import server.ScravaGrpc;
import server.ScravaGrpc.ScravaBlockingStub;
import server.ScravaProto.ClientData;
import server.ScravaProto.ClientLogin;
import server.ScravaProto.ClientRegister;
import server.ScravaProto.Query;
import server.ScravaProto.SerializedObject;

public class ClientController {
	public static final ClientController INSTANCE = new ClientController("127.0.0.1", 8080);
	
	private ScravaBlockingStub blockingStub;
	private ClientController(String ip, int port) {
		 ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).useTransportSecurity().build();
		 blockingStub = ScravaGrpc.newBlockingStub(channel);	
	}
	
	public ClientData login(ClientLogin cl) {
		return blockingStub.login(cl);
	}
	
	public ClientData register(ClientRegister cr) {
		return blockingStub.register(cr);
	}
	
	public void saveProject(String serialized) {
		saveProject(SerializedObject.newBuilder().setObj(serialized).build());
	}
	
	public void saveProject(SerializedObject so) {
		blockingStub.saveProject(so);
	}
	
	public List<String> getTutorialList(Query q) {
		return blockingStub.getTutorialList(q).getResultsList();
	}
	
}
