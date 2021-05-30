package finalPractice10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;


public class DigitSign {
   // 송신자의 공개키, 개인키
   final static String RSA_ALGO = "SHA1withRSA";
   private static final String AES_ALGO = "AES";
   KeyManager manager=new KeyManager();
   /* 전자봉투 생성_ 전자서명할 원본 데이터를 읽어오고, 서명에 쓰일 private key를 역직렬화로 가져오고 signature 직렬화 */
   public void sign(String dataFilename, String privateKeyFilename, String sigFilename)
         throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
      Signature sig = Signature.getInstance(RSA_ALGO); // RSA

      /* 전자서명할 원본 데이터 읽어오기 */
      Path path = (new File(dataFilename)).toPath();
      byte[] data = Files.readAllBytes(path); /* 서명에 쓰일 privateKey를 역직렬화로 가져오기 */
      PrivateKey key = manager.restorePrivateKey(privateKeyFilename);

      sig.initSign(key);
      sig.update(data);

      byte[] signature = sig.sign(); // 서명 수행

      // signature 직렬화
      try (FileOutputStream fstream = new FileOutputStream(sigFilename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(signature);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @SuppressWarnings("finally")
   public Boolean verify(String descryptedDataFilename, String descryptedSigFilename,
         String descryptedPublicKeyFilename) throws NoSuchAlgorithmException, FileNotFoundException, IOException,
         ClassNotFoundException, InvalidKeyException, SignatureException {
      Signature sig = Signature.getInstance(RSA_ALGO); // "SHA1withRSA"

      boolean rslt = false;
      try (FileInputStream fis = new FileInputStream(descryptedSigFilename)) {
         try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            byte[] signature = (byte[]) obj;

            PublicKey key =  manager.restorePublicKey(descryptedPublicKeyFilename); // publicKey 역직렬화

            Path path = (new File(descryptedDataFilename)).toPath();
            byte[] data = Files.readAllBytes(path);

            sig.initVerify(key);
            sig.update(data);

            rslt = sig.verify(signature);

         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      } finally {
         return rslt;
      }
   }

   public void encrypt(String targetFilename, String secretKeyFilename, String encryptedTargetFilename)
         throws Exception {
      // 보내는 사람의 AES가 저장된 파일을 읽어 가져오기
      SecretKey secretKey =  manager.restoreSecretKey(secretKeyFilename);
      Cipher c = Cipher.getInstance(AES_ALGO);
      c.init(Cipher.ENCRYPT_MODE, secretKey);

      // filename 역직렬화로 암호화할 데이터 가져오고, 그 데이터를 doFinal의 매개변수로 넣기
      Path path = (new File(targetFilename)).toPath();
      byte[] target = Files.readAllBytes(path);
      byte[] encVal = c.doFinal(target); // encVal : AES로 암호화 된 보낼 데이터

      // encryptedDataFilename : AES로 암호화 된 데이터를 담을 filename, 암호화한 원본 데이터를 file에 담기
      try (FileOutputStream fstream = new FileOutputStream(encryptedTargetFilename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(encVal);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void encryptPublicKey(String publicKeyFilename, String secretKeyFilename,
         String encryptedPublicKeyFilename) throws Exception {
	
      // 보내는 사람의 AES가 저장된 파일을 읽어 가져오기
      SecretKey secretKey = manager.restoreSecretKey(secretKeyFilename);
      Cipher c = Cipher.getInstance(AES_ALGO);
      c.init(Cipher.ENCRYPT_MODE, secretKey);

      // filename 역직렬화로 암호화할 데이터 가져오고
      // 그 데이터를 doFinal의 매개변수로 넣기
      PublicKey publicKey = manager.restorePublicKey(publicKeyFilename);
      byte[] encVal = c.doFinal(publicKey.getEncoded()); // encVal : AES로 암호화 된 보낼 데이터

      try (FileOutputStream fstream = new FileOutputStream(encryptedPublicKeyFilename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(encVal);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
