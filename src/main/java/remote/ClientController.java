package remote;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import server.ScravaGrpc;
import server.ScravaGrpc.ScravaBlockingStub;
import server.ScravaProto.CipherUpdate;
import server.ScravaProto.ClientData;
import server.ScravaProto.ClientLogin;
import server.ScravaProto.ClientRegister;
import server.ScravaProto.EmptyMessage;
import server.ScravaProto.Query;
import server.ScravaProto.SudoMessage;

public class ClientController {
	public static final ClientController INSTANCE = new ClientController("127.0.0.1", 8080);
	private Cipher encryptCipher;
	private PublicKey publicKey;
	private String token;
	
	private ScravaBlockingStub blockingStub;
	private ClientController(String ip, int port) {
		try {
			encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
			blockingStub = ScravaGrpc.newBlockingStub(channel);
			CipherUpdate cu = 	blockingStub.startConnection(EmptyMessage.getDefaultInstance());
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(cu.getPublicKey().toByteArray()));
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		} catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) { e.printStackTrace(); }
	}
	
	private void refreshCypher() {
		CipherUpdate cu = blockingStub.refreshCypher(EmptyMessage.getDefaultInstance());
		try {
			publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(cu.getPublicKey().toByteArray())));
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
		return blockingStub.login(
				ClientLogin.newBuilder()
						   .setName(name)
						   .setPassword(rsaEncode(pw))
						   .build()
				);
	}
	
	public ClientData register(ClientRegister cr) {
		return blockingStub.register(cr);
	}
	
	public void saveProject(String serialized) {
		saveProject(SudoMessage.newBuilder().setToken(token).setObj(serialized).build());
	}
	
	public void saveProject(SudoMessage so) {
		blockingStub.saveProject(so);
	}
	
	public List<String> getTutorialList(Query q) {
		return blockingStub.getTutorialList(q).getResultsList();
	}
	
	public static void main(String[] args) {
		ClientController.INSTANCE.login("test", "test");
	}
	
}
