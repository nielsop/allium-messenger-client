package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

import java.nio.charset.StandardCharsets;

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
        return ByteString.copyFrom(encryptionService.decryptData(data.toByteArray()));
    }

    @Override
    public ByteString encryptData(ByteString data, byte[] publicKey) {
        return ByteString.copyFrom(encryptionService.encryptData(publicKey, data.toByteArray()));
    }

    @Override
    public byte[] getPublicKey() {
        return encryptionService.getPublicKey();
    }
}
