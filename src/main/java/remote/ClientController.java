package remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import com.google.protobuf.ByteString;

import domain.AppCache;
import domain.Project;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import server.ScravaGrpc;
import server.ScravaGrpc.ScravaBlockingStub;
import server.ScravaProto.AuthoredObject;
import server.ScravaProto.CipherUpdate;
import server.ScravaProto.ClientData;
import server.ScravaProto.ClientLogin;
import server.ScravaProto.ClientRegister;
import server.ScravaProto.EmptyMessage;
import server.ScravaProto.ObjectDescriptor;
import server.ScravaProto.Query;
import server.ScravaProto.SerializedObject;
import server.ScravaProto.TokenMessage;
import ui.components.UserPanel;

public class ClientController {
	public static final ClientController INSTANCE = new ClientController("127.0.0.1", 8080);
	private Cipher encryptCipher;
	private PublicKey publicKey;
	private ByteString pk = null;
	
	private ScravaBlockingStub blockingStub;
	private ClientController(String ip, int port) {
		try {
			encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
			blockingStub = ScravaGrpc.newBlockingStub(channel);
			ClientData cacheUser = AppCache.getInstance().user;
			CipherUpdate cu;
			try {
				cu = blockingStub.startConnection(TokenMessage.newBuilder().setToken(cacheUser.getToken()).setUid(cacheUser.getId()).build());
			} catch(StatusRuntimeException | NullPointerException e) {
				AppCache.getInstance().user = null;
				AppCache.save();
				UserPanel.INSTANCE.regenerate();
				cu = blockingStub.startConnection(TokenMessage.newBuilder().build());
			}
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cu.getPublicKey().toByteArray()));
			pk = cu.getPublicKey();
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) { e.printStackTrace(); }
	}
	
	private void refreshCypher() {
		CipherUpdate cu = blockingStub.refreshCypher(EmptyMessage.getDefaultInstance());
		try {
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cu.getPublicKey().toByteArray()));
			pk = cu.getPublicKey();
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | InvalidKeyException e) { e.printStackTrace(); }
	}
	
	private String rsaEncode(String orig) {
		try {
			return new String(Base64.getEncoder().encode(encryptCipher.doFinal(orig.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public ClientData login(String name,  String pw) {
		try {
			AppCache.getInstance().user = blockingStub.login(
					ClientLogin.newBuilder()
					   .setName(name)
					   .setPassword(rsaEncode(pw))
					   .setPk(pk)
					   .build()
			);
		} catch(StatusRuntimeException e) {
			if(e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION) {
				refreshCypher();
				return login(name, pw);
			} else
				throw e;
		}
		AppCache.save();
		return AppCache.getInstance().user;
	}
	
	public ClientData register(String name, String pw) {
		try {
			AppCache.getInstance().user = blockingStub.register(
					ClientRegister.newBuilder()
					   .setName(name)
					   .setPassword(rsaEncode(pw))
					   .setPk(pk)
					   .build()
			);
		} catch(StatusRuntimeException e) {
			if(e.getStatus().getCode() == Status.Code.FAILED_PRECONDITION) {
				refreshCypher();
				return register(name, pw);
			} else
				throw e;
		}
		AppCache.save();
		return AppCache.getInstance().user;
	}	
	
	public Iterator<ObjectDescriptor> getUserProjects(int offset) {
		return blockingStub.getProjectList(Query.newBuilder().setQuery("author = " + AppCache.getInstance().user.getId()).setOffset(0).build());
	}
	
	public void logoff() {
		AppCache ac = AppCache.getInstance();
		blockingStub.deleteToken(TokenMessage.newBuilder().setToken(ac.user.getToken()).setUid(ac.user.getId()).build());
		ac.user = null;
		AppCache.save();
	}
	
	public void saveProject(Project p) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream( baos )){
		ClientData account = AppCache.getInstance().user;        
        oos.writeObject(p);
        oos.close();
		ObjectDescriptor od = saveProject(
				AuthoredObject.newBuilder()
							  .setToken(account.getToken())
							  .setUid(account.getId())
							  .setObj(SerializedObject.newBuilder()
									  				  .setId(p.id)
									  				  .setName(p.name)
									  				  .setObj(Base64.getEncoder().encodeToString(baos.toByteArray()))
									  ).build());
		p.id = od.getId();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to save project: " + e.getMessage());
		}
	}
	
	public void deleteProject(ObjectDescriptor p) {
		ClientData cd = AppCache.getInstance().user;
		blockingStub.deleteProject(AuthoredObject.newBuilder()
												 .setToken(cd.getToken())
												 .setUid(cd.getId())
												 .setObj(SerializedObject.newBuilder().setId(p.getId()))
												 .build());
	}
	
	public ObjectDescriptor saveProject(AuthoredObject so) {
		return blockingStub.saveProject(so);
	}
	
	public Project getProject (int id) {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(
												Base64.getDecoder()
													  .decode(
															  blockingStub.getProject(
																	  Query.newBuilder()
																	  	   .setQuery(""+id)
																	  	   .build()
															  ).getObj()
													   ));
				ObjectInputStream ois = new ObjectInputStream( bais )){
			return (Project) ois.readObject();
		} catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to save project: " + e.getMessage());
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); //Really unlike
			return null;
		}
	}
	public static record Tutorial(String title, String content) {}
	public Tutorial getTutorial(int id) {
		SerializedObject so = blockingStub.getTutorial(Query.newBuilder().setQuery(""+id).build());
		return new Tutorial(so.getName(), so.getObj());
	}
	
	public Iterator<ObjectDescriptor> getTutorialList(Query q) {
		return blockingStub.getTutorialList(q);
	}
	
	public static void main(String[] args) {
		System.out.println(ClientController.INSTANCE.login("test", "test"));
	}
	
}
