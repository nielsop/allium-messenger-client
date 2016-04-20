package nl.han.asd.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public interface IEncrypt {
    ByteString encryptData(String data, String publicKey);
}
