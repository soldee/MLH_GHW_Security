import com.soldee.mlh.AESEncryption;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import static org.junit.Assert.*;

public class AESTest extends RunListener {

    private final URL path;

    public AESTest() {
        ClassLoader loader = AESTest.class.getClassLoader();
        this.path = loader.getResource("");
    }

    @Test
    public void testAESEncryption() throws Exception {
        // generate text to encrypt
        FileOutputStream fileOutputStream = new FileOutputStream(path.getPath()+"/text.txt");
        fileOutputStream.write("text to encrypt using AES".getBytes(StandardCharsets.UTF_8));

        // generate key and use it to encrypt and decrypt the file
        AESEncryption.main(new String[]{"-genkey", path.getPath()+"/keyFile"});
        AESEncryption.main(new String[]{"-encrypt", path.getPath()+"/text.txt", path.getPath()+"/encrypted.txt", path.getPath()+"/keyFile"});
        AESEncryption.main(new String[]{"-decrypt", path.getPath()+"/encrypted.txt", path.getPath()+"/decrypted.txt", path.getPath()+"/keyFile"});

        // compare the original text to the decrypted
        byte[] inOriginal = Files.readAllBytes(new File(path.getPath()+"/text.txt").toPath());
        byte[] inDecrypted = Files.readAllBytes(new File(path.getPath()+"/decrypted.txt").toPath());

        assertArrayEquals(inOriginal, inDecrypted);
    }


    @Override
    public void testRunFinished(Result result) throws Exception {
        new File(path.getPath()+"/keyFile").delete();
        new File(path.getPath()+"/text.txt").delete();
        new File(path.getPath()+"/encrypted.txt").delete();
        new File(path.getPath()+"/decrypted.txt").delete();
    }

}
