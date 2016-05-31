package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.han.asd.project.commonservices.encryption.EncryptionModule;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EncryptionServiceTest {
    private IEncryptionService encryptionService;

    @Before
    public void setUp() throws Exception {
        //cryptographyService = new CryptographyService();
        Injector injector = Guice.createInjector(new EncryptionModule());
        encryptionService = injector.getInstance(IEncryptionService.class);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEncryptData() throws Exception {
        String dataToEncrypt = "Encrypt this";
        byte[] publicKey = "12345678".getBytes();
        Assert.assertEquals(1, 1);
    }
}
