package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

/**
 * Created by Niels Bokmans on 12-4-2016.
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
        return ByteString.copyFrom(encryptionService.decrypt(data.toByteArray()));
    }

    @Override
    public ByteString encryptData(ByteString data, String publicKey) {
            return null;
//        return ByteString.copyFrom(encryptionService.encryptData(strPublicKey, data.toByteArray()));
    }

    @Override
    public String getPublicKey() {
        return encryptionService.getPublicKey().toString();
    }
}
