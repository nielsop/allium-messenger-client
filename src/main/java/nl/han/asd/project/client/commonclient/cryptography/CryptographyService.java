package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public class CryptographyService implements IEncrypt, IDecrypt {

    @Override
    public ByteString encryptData(ByteString data, ByteString publicKey) {
        return null;
    }
}
