package nl.han.asd.project.client.commonclient.cryptography;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EncryptionServiceTest {
    private EncryptionService encryptionService;

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
        //        ByteString encryptedData = cryptographyService.encryptData(ByteString.copyFromUtf8(dataToEncrypt),publicKey);

        Assert.assertEquals(1, 1); //TODO: Testcase afmaken!
        // Assert.assertEquals(encryptedData,dataToEncrypt);
    }
}
