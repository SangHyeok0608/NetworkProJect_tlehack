import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FileEncryption {
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    private static final String PADDING = "AES/ECB/PKCS5Padding";

    private static final String savePath = "Download" + File.separator;

    public static File encryptFile(File inputFile, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        String outputfile = null;

        try {
            outputfile = savePath + getEncryptedFileName(inputFile);
            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputfile);
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] inputBytes = new byte[16];
            int bytesRead;
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(padBytes(inputBytes, bytesRead));
                outputStream.write(outputBytes);
            }
            byte[] finalBytes = cipher.doFinal();
            outputStream.write(finalBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(outputfile);
    }

    public static File decryptFile(File encryptedFile, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        String outputfile = null;

        try {
            outputfile = savePath + getDecryptedFileName(encryptedFile);
            FileInputStream inputStream = new FileInputStream(encryptedFile);
            FileOutputStream outputStream = new FileOutputStream(outputfile);
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] inputBytes = new byte[16];
            int bytesRead;
            while ((bytesRead = inputStream.read(inputBytes)) != -1) {
                byte[] outputBytes = cipher.update(inputBytes, 0, bytesRead);
                outputStream.write(outputBytes);
            }
            byte[] finalBytes = cipher.doFinal();
            outputStream.write(finalBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new File(outputfile);
    }

    public static String encryptString(String input, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);

        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decryptString(String input, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);

        try {
            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = Base64.getDecoder().decode(input);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getEncryptedFileName(File file) {
        return "encrypted_" + file.getName();
    }

    private static String getDecryptedFileName(File file) {
        String fileName = file.getName();
        return "decrypted_" + fileName.substring(fileName.indexOf("_") + 1);
    }

    private static byte[] padBytes(byte[] inputBytes, int bytesRead) {
        byte[] paddedBytes = new byte[16];
        System.arraycopy(inputBytes, 0, paddedBytes, 0, bytesRead);
        int paddingLength = 16 - bytesRead;
        for (int i = bytesRead; i < 16; i++) {
            paddedBytes[i] = (byte) paddingLength;
        }
        return paddedBytes;
    }
}
