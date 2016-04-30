package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 29-4-2016
 */
public class CryptographyService implements IEncrypt, IDecrypt {

    /**
     * Decrypts a byte array containing data.
     *
     * @param data The byte array in ByteString format.
     * @return The decrypted byte array in ByteString format.
     */
    @Override
    public ByteString decryptData(ByteString data) {
        return null;
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
    }
}
