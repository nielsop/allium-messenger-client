package nl.han.asd.project.client.commonclient.cryptography;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import nl.han.asd.project.commonservices.encryption.IEncryptionService;

import java.nio.charset.StandardCharsets;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class CryptographyService implements IEncrypt, IDecrypt, IPublicKey {
    private IEncryptionService encryptionService;

    @Inject
    public CryptographyService(final IEncryptionService encryptionService)
    {
        this.encryptionService = encryptionService;
    }

	 /**
     * Decrypts a byte array containing data.
     *
     * @param data The byte array in ByteString format.
     * @return The decrypted byte array in ByteString format.
     */
    @Override
    public ByteString decryptData(ByteString data) {
        return null;
       // return ByteString.copyFrom(encryptionService.decryptData(data.toByteArray()));
    }

	
    /**
     * Encrypts a byte array containing data.
     *
     * @param data      The byte array in ByteString format.
     * @param publicKey The public key to encrypt the ByteString with.
     * @return The encrrypted byte array in ByteString format.
     */
    @Override
    public ByteString encryptData(ByteString data, byte[] publicKey) {
        return null;
     //   return ByteString.copyFrom(encryptionService.encryptData(publicKey, data.toByteArray()));
    }
	
    public byte[] getPublicKey() {
    //    return encryptionService.getPublicKey();
        return null;
    }
}
