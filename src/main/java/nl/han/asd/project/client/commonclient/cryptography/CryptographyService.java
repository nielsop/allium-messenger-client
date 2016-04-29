package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public class CryptographyService implements IEncrypt, IDecrypt {

    @Override
    public ByteString decryptData(ByteString data) {
        return null;
    }

    @Override
    public ByteString encryptData(ByteString data, byte[] publicKey) {
        return null;
    }
}
