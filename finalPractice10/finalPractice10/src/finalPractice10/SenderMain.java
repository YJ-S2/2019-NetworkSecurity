package finalPractice10;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.util.HashMap;
import java.util.Scanner;

public class SenderMain {
   public HashMap<String, String> main(String receiverPublicKey) throws Exception {

      

      HashMap<String, String> map = new HashMap<String, String>();

      Scanner scan = new Scanner(System.in);

      System.out.println("<송신자측>");

      System.out.print("데이터를 저장할 파일경로를 입력해주세요: ");
      String dataFile = scan.nextLine();

      System.out.print("파일에 저장할 내용을 입력해주세요: ");
      String data = scan.nextLine();

      try (FileOutputStream fstream = new FileOutputStream(dataFile)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(data);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }

      System.out.print("공개키를 저장할 파일경로를 입력해주세요: ");
      String senderPublicKeyFile = scan.nextLine();

      System.out.print("개인키를 저장할 파일경로를 입력해주세요: ");
      String senderPrivateKeyFile = scan.nextLine();

      System.out.println("공개키와 개인키를 생성중입니다. . .");
      
      DigitSign sign = new DigitSign();
      DigitEnvelope envelope = new DigitEnvelope();
      
      // 송신자의 키 생성
      MyKeyPair keyPair = new MyKeyPair();
      keyPair.SenderCreateAndSaveKeys(senderPublicKeyFile, senderPrivateKeyFile);
      System.out.println("공개키와 개인키를 생성하였습니다.");

      MySecretKey secretKey = new MySecretKey();
      System.out.print("비밀키를 저장할 파일경로를 입력해주세요: ");
      String secretKeyFile = scan.nextLine();

      System.out.println("비밀키를 생성중입니다. . .");
      secretKey.generateAndSaveKey(secretKeyFile);
      System.out.println("비밀키를 생성하였습니다.");

      System.out.print("암호화된 데이터를 저장할 파일경로를 입력해주세요: ");
      String encryptedDataFile = scan.nextLine();

      map.put(Practice10.encryptedDataFileKey, encryptedDataFile);

      // 원본 데이터를 비밀키로 암호화하여 암호화된 파일로 저장
      sign.encrypt(dataFile, secretKeyFile, encryptedDataFile);
      System.out.println("원본데이터를 암호화하여 저장이 완료되었습니다.");

      System.out.print("전자서명을 저장할 파일경로를 입력해주세요: ");
      String sigDataFile = scan.nextLine();

      System.out.print("개인키로 전자서명을 생성중입니다.  ");
      sign.sign(dataFile, senderPrivateKeyFile, sigDataFile);
      System.out.println("전자서명 생성이 완료되었습니다.");

      System.out.print("암호화된 전자서명을 저장할 파일경로를 입력해주세요: ");
      String encryptedSigFile = scan.nextLine();
      map.put(Practice10.encryptedSigFileKey, encryptedSigFile);

      sign.encrypt(sigDataFile, secretKeyFile, encryptedSigFile);
      System.out.println("전자서명이 비밀키로 암호화가 완료되었습니다.");

      // 공개키를 비밀키로 암호화
      System.out.print("비밀키를 사용하여 암호화한 공개키를 저장할 파일경로를 입력해주세요: ");
      String encryptedPublicKeyFile = scan.nextLine();
      map.put(Practice10.encryptedPublicKeyFileKey, encryptedPublicKeyFile);

      sign.encryptPublicKey(senderPublicKeyFile, secretKeyFile, encryptedPublicKeyFile);

      System.out.println("비밀키로 공개키를 암호화 완료되었습니다.");

      System.out.print("암호화된 비밀키를 저장할 파일경로를 입력해주세요: ");
      String encryptedSecretKeyFile = scan.next();
      map.put(Practice10.encryptedSecretKeyFileKey, encryptedSecretKeyFile);

      System.out.println("비밀키를 수신자의 공개키로 암호화 중입니다 . . . ");
      // 비밀키를 수신자의 공개키로 암호화하여 파일에 저장
      envelope.envelope(secretKeyFile, receiverPublicKey, encryptedSecretKeyFile);
      System.out.println("비밀키가 수신자의 공개키로 암호화 되었습니다.");

      // scan.close();
      return map;
   }
}