package finalPractice10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DigitEnvelope {
	// 복호화한 파일 이름

	final static String RSA_ENVEL_ALGO = "RSA/ECB/PKCS1Padding";
	private static final String AES_ALGO = "AES";

	KeyManager manager = new KeyManager();

	// RSA/ECB/PKCS1Padding
	/* 전자봉투 생성(secretKey를 receiverPublicKey로 암호화하여 파일에 저장 */
	
	public void envelope(String secretFilename, String receiverPublicKeyFilename, String encryptedSecretFilename)
	
	
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		KeyManager manager = new KeyManager();
		PublicKey receiverPublicKey = manager.restorePublicKey(receiverPublicKeyFilename);
		Cipher c = Cipher.getInstance(RSA_ENVEL_ALGO);

		c.init(Cipher.ENCRYPT_MODE, receiverPublicKey);

		SecretKey targetSecretKey = manager.restoreSecretKey(secretFilename);
		byte[] rslt = c.doFinal(targetSecretKey.getEncoded()); /* 반환값 : byte[] */

		// 암호화한 비밀키 저장
		SecretKey encSecretKey = new SecretKeySpec(rslt, 0, rslt.length, "AES/padding");
		manager.saveSecretKey(encSecretKey, encryptedSecretFilename);
	}

	/* 수신자 privateKey로 비밀키 복호화한 후 리턴 */
	
	public void decSecretKey(String encryptedSecretKeyFilename, String ReceiverPrivateKeyFilename,
		                                                    	String descryptedSecretKeyFilename) throws Exception {

		PrivateKey key = manager.restorePrivateKey(ReceiverPrivateKeyFilename);
		SecretKey encryptedSecretKey = manager.restoreSecretKey(encryptedSecretKeyFilename);

		// 수신자의 개인키로 복호화
		Cipher c = Cipher.getInstance(RSA_ENVEL_ALGO);
		c.init(Cipher.DECRYPT_MODE, key);

		byte[] decValue = c.doFinal(encryptedSecretKey.getEncoded());

		SecretKey decSecretKey = new SecretKeySpec(decValue, 0, decValue.length, "AES");
		manager.saveSecretKey(decSecretKey, descryptedSecretKeyFilename);
	}

	public void decPublicKeyAES(String encryptedPublicKeyFilename, String descryptedSecretKeyFilename,
			String descryptedPublicKeyFilename) throws Exception {
		SecretKey secretKey = manager.restoreSecretKey(descryptedSecretKeyFilename);
		Cipher c = Cipher.getInstance(AES_ALGO);
		c.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] encPublicKey = null;
		try (FileInputStream fis = new FileInputStream(encryptedPublicKeyFilename)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object obj = ois.readObject();
				encPublicKey = (byte[]) obj;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] rslt = c.doFinal(encPublicKey);

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(rslt);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey decPublicKey = keyFactory.generatePublic(pubKeySpec);
		manager.savePublicKey(decPublicKey, descryptedPublicKeyFilename);
	}

	public void decAES(String encryptedFilename, String descryptedSecretKeyFilename, String descryptedFilename)
			throws Exception {
		SecretKey secretKey = manager.restoreSecretKey(descryptedSecretKeyFilename);
		Cipher c = Cipher.getInstance(AES_ALGO);
		c.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] target = null;
		try (FileInputStream fis = new FileInputStream(encryptedFilename)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object obj = ois.readObject();
				target = (byte[]) obj;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		byte[] rslt = c.doFinal(target);

		FileOutputStream output = new FileOutputStream(new File(descryptedFilename));
		output.write(rslt);
		output.close();
	}
}