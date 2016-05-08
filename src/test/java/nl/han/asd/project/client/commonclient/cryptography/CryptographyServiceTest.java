package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Julius on 18/04/16.
 */
public class CryptographyServiceTest {
    private CryptographyService cryptographyService;

    @Before
    public void setUp() throws Exception {
        //cryptographyService = new CryptographyService();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEncryptData() throws Exception {
        String dataToEncrypt = "Encrypt this";
        byte[] publicKey = "12345678".getBytes();
        ByteString encryptedData = cryptographyService.encryptData(ByteString.copyFromUtf8(dataToEncrypt),publicKey);

        // Assert.assertEquals(encryptedData,dataToEncrypt);
    }
}
