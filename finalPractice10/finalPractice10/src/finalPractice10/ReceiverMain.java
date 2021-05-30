package finalPractice10;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class ReceiverMain {
   private String ReceiverPrivateKeyFile = null;
   public String getPubKeyPath() {
      Scanner scan = new Scanner(System.in);

      System.out.println("<수신자측>");
      
      System.out.print("공개키를 저장할 파일경로를 입력해주세요: ");
      String ReceiverPublicKeyFile = scan.nextLine();
      System.out.print("개인키를 저장할 파일경로를 입력해주세요: ");
      ReceiverPrivateKeyFile = scan.nextLine();

      MyKeyPair keyPair = new MyKeyPair();
      keyPair.ReceiverCreateAndSaveKeys(ReceiverPublicKeyFile, ReceiverPrivateKeyFile);

      return ReceiverPublicKeyFile;

   }

   public void main(HashMap<String, String> filenames) throws Exception {
      DigitEnvelope envelope = new DigitEnvelope();

      Scanner scan = new Scanner(System.in);
      System.out.println("<수신자측>");
      
      System.out.print("암호화된 파일을 받으시겠습니까?(예-1 / 아니오-2): ");
      int agree = scan.nextInt();
      switch (agree) {
      case 1: {

         System.out.print("해독한 비밀키를 저장할 파일경로를 입력해주세요: ");
         String decryptedSecretKeyFilename = scan.next();
         envelope.decSecretKey(filenames.get(Practice10.encryptedSecretKeyFileKey), ReceiverPrivateKeyFile,
               decryptedSecretKeyFilename);

         System.out.print("해독한 송신자 공개키를 저장할 파일경로를 입력해주세요: ");
         String decryptedPublicKeyFilename = scan.next();
         envelope.decPublicKeyAES(filenames.get(Practice10.encryptedPublicKeyFileKey), decryptedSecretKeyFilename,
               decryptedPublicKeyFilename);

         System.out.print("해독한 전자서명을 저장할 파일경로를 입력해주세요: ");
         String descryptedSigFilename = scan.next();
         envelope.decAES(filenames.get(Practice10.encryptedSigFileKey), decryptedSecretKeyFilename,
               descryptedSigFilename);

         System.out.print("해독한 데이터파일을 저장할 파일경로를 입력해주세요: ");
         String decryptedDataFilename = scan.next();
         envelope.decAES(filenames.get(Practice10.encryptedDataFileKey), decryptedSecretKeyFilename,
               decryptedDataFilename);

         DigitSign sign = new DigitSign();
         boolean result = sign.verify(decryptedDataFilename, descryptedSigFilename, decryptedPublicKeyFilename);
         System.out.print("수신자는 성공적으로 data를 전송 받았는가? ");
         System.out.println(result);

         System.out.println("----수신한 데이터----");
         
         String content = new String(Files.readAllBytes(Paths.get(decryptedDataFilename)));
         System.out.println(content);
         System.out.println("\nBye");
         break;
      }
      case 2: {
         System.out.println("Bye");
         break;
      }
      }

   }
}