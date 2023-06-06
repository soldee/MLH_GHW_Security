package com.soldee.mlh;

import java.io.*;
import java.security.*;
import javax.crypto.*;


public class AESEncryption {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {

        int mode;

        switch (args[0]) {
            case "-genkey":
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                SecureRandom random = new SecureRandom();

                keyGenerator.init(random);
                SecretKey key = keyGenerator.generateKey();

                try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[1]))) {
                    out.writeObject(key);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            case "-encrypt":
                mode = Cipher.ENCRYPT_MODE;
                break;
            case "-decrypt":
                mode = Cipher.DECRYPT_MODE;
                break;
            default:
                printHelp();
                return;
        }

        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(args[3]));
        FileInputStream in = new FileInputStream(args[1]);
        FileOutputStream out = new FileOutputStream(args[2]);
        Key key = (Key) keyIn.readObject();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, key);
        crypt(in,out,cipher);
    }

    private static void printHelp() {
        System.out.println(new StringBuilder()
                .append("Usage:")
                .append("\tjava AESEncryption -genkey [key_file_output_path]")
                .append("\tjava AESEncryption -encrypt [input_file_to_encrypt] [out_encrypted_text] [key_file_path]")
                .append("\tjava AESEncryption -decrypt [input_file_encrypted] [out_decrypted_text] [key_file_path]")
        );
    }

    private static void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean done = false;
        while (!done) {
            inLength = in.read(inBytes);

            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else done = true;
        }

        if (inLength > 0) outBytes = cipher.doFinal(inBytes, 0, inLength);
        else outBytes = cipher.doFinal();
        out.write(outBytes);
    }
}