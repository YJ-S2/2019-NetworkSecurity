package finalPractice10;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MySecretKey {
   private static final String AESALGO = "AES";
   private static final byte[] keyValue = { 'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e','y'};

   public void generateAndSaveKey(String secretKeyFilename) throws Exception {
	   KeyManager manager=new KeyManager();
	   SecretKey secretKey = new SecretKeySpec(keyValue, AESALGO);
       manager.saveSecretKey(secretKey, secretKeyFilename);
   }

}