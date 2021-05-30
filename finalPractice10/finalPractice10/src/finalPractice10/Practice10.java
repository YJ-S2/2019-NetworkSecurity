package finalPractice10;
import java.util.HashMap;


public class Practice10 {
   public static final String encryptedSigFileKey = "encryptedSigFile";
   public static final String encryptedDataFileKey = "encryptedDataFile";
      public static final String encryptedPublicKeyFileKey = "encryptedPublicKeyFile";
      public static final String encryptedSecretKeyFileKey = "encryptedSecretKeyFile";
   public static void main(String[] args) throws Exception {
      ReceiverMain receiver = new ReceiverMain();
      String recvPubPath = receiver.getPubKeyPath();
      
      SenderMain sender = new SenderMain();
      HashMap<String, String> filenames = sender.main(recvPubPath);
      
      receiver.main(filenames);
   }
}