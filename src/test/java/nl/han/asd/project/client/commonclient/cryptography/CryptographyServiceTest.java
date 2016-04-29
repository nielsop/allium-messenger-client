package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;
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
        String publicKey = "12345678";
        //ByteString encryptedData = cryptographyService.encryptData(dataToEncrypt,publicKey);

        // Assert.assertEquals(encryptedData,dataToEncrypt);
    }
}
