package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * @author Julius
 * @version 1.0
 * @since 29/04/16
 */
@FunctionalInterface
public interface IEncrypt {
    ByteString encryptData(ByteString data, byte[] publicKey);
}
