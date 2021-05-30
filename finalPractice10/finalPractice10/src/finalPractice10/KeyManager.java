package finalPractice10;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

public class KeyManager {
   /* Key ����ȭ*/
   public void savePublicKey(PublicKey publicKey, String filename) {
      try (FileOutputStream fstream = new FileOutputStream(filename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(publicKey);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void savePrivateKey(PrivateKey privateKey, String filename) {
      try (FileOutputStream fstream = new FileOutputStream(filename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(privateKey);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /* Key ������ȭ */
   @SuppressWarnings("finally")
   public 
   PublicKey restorePublicKey(String filename) throws FileNotFoundException, IOException {
      PublicKey publickey = null;
      try (FileInputStream fis = new FileInputStream(filename)) {
         try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            publickey = (PublicKey) obj;

         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            return publickey;
         }
      }
   }

   @SuppressWarnings("finally")
   public PrivateKey restorePrivateKey(String filename) throws FileNotFoundException, IOException {
      PrivateKey privatekey = null;
      try (FileInputStream fis = new FileInputStream(filename)) {
         try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            privatekey = (PrivateKey) obj;

         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            return privatekey;
         }
      }
   }

   /* SecretKey ����ȭ */
   public void saveSecretKey(SecretKey secretKey, String filename) {
      try (FileOutputStream fstream = new FileOutputStream(filename)) {
         try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
            ostream.writeObject(secretKey);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /* SecretKey ������ȭ */
   @SuppressWarnings("finally")
   public SecretKey restoreSecretKey(String filename) throws FileNotFoundException, IOException {
      SecretKey key = null;
      try (FileInputStream fis = new FileInputStream(filename)) {
         try (ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            key = (SecretKey) obj;
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            return key;
         }
      }
   }
}