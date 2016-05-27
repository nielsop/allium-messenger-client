package nl.han.asd.project.client.commonclient.cryptography;

import nl.han.asd.project.commonservices.encryption.IEncryptionService;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class EncryptionService implements IEncryptionService {
    /**
     * Decrypts a byte array containing data.
     *
     * @param data The byte array in ByteString format.
     * @return The decrypted byte array in ByteString format.
     */
    @Override
    public byte[] decryptData(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] getPublicKey() {
        return new byte[0];
    }

    /**
     * Encrypts a byte array containing data.
     *
     * @param key  The public key to encrypt the ByteString with.
     * @param data The byte array in ByteString format.
     * @return The encrrypted byte array in ByteString format.
     */
    @Override
    public byte[] encryptData(byte[] key, byte[] data) {
        return new byte[0];
    }
}
