package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public interface IEncrypt {
    ByteString encryptData(ByteString data, ByteString publicKey);
}
