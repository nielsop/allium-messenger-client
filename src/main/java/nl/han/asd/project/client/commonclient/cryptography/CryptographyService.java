package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Marius on 25-04-16.
 */
public class CryptographyService implements IEncrypt, IDecrypt, IPublicKey {
    private IEncryptionService encryptionService;

    @Inject
    public CryptographyService(final IEncryptionService encryptionService)
    {
        this.encryptionService = encryptionService;
    }

    @Override
    public ByteString decryptData(ByteString data) {
        return null;
       // return ByteString.copyFrom(encryptionService.decryptData(data.toByteArray()));
    }

    @Override
    public ByteString encryptData(ByteString data, byte[] publicKey) {
        return null;
     //   return ByteString.copyFrom(encryptionService.encryptData(publicKey, data.toByteArray()));
    }

    @Override
    public byte[] getPublicKey() {
    //    return encryptionService.getPublicKey();
        return null;
    }
}
