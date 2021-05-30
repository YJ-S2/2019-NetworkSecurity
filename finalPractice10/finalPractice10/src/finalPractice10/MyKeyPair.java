package finalPractice10;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MyKeyPair implements Serializable {

   private static final long serialVersionUID = 1L;
   private static final String keyAlgorithm = "RSA";

   private KeyPairGenerator keyGen;
   private KeyPair pair;
   private PrivateKey privateKey;
   private PublicKey publicKey;

   public MyKeyPair getInstance(int keylength) throws NoSuchAlgorithmException {
      MyKeyPair rslt = new MyKeyPair();

      rslt.keyGen = KeyPairGenerator.getInstance(keyAlgorithm);
      rslt.keyGen.initialize(keylength);

      return rslt;
   }

   /* 송신자의 키 쌍 */
   public void SenderCreateAndSaveKeys(String SenderPublicKeyFilename, String SenderPrivateKeyFilename) {
      MyKeyPair keyPair = new MyKeyPair();
      try {
         keyPair = keyPair.getInstance(2048);
         keyPair.createAndSaveKeys(SenderPublicKeyFilename, SenderPrivateKeyFilename);
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /* 수신자의 키 쌍 */
   public void ReceiverCreateAndSaveKeys(String ReceiverPublicKeyFilename, String ReceiverPrivateKeyFilename) {
      MyKeyPair keyPair = new MyKeyPair();
      try {
         keyPair = keyPair.getInstance(2048);
         keyPair.createAndSaveKeys(ReceiverPublicKeyFilename, ReceiverPrivateKeyFilename);
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   public void createAndSaveKeys(String publicFilename, String privateFilename) {
	      this.pair = this.keyGen.generateKeyPair();
	      KeyManager manager=new KeyManager();

	      this.publicKey = pair.getPublic();
	      manager.savePublicKey(this.publicKey, publicFilename);

	      this.privateKey = pair.getPrivate();
	      manager.savePrivateKey(this.privateKey, privateFilename);
	   }

}