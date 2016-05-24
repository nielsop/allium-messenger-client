package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
@FunctionalInterface
public interface IDecrypt {
    ByteString decryptData(ByteString data);
}
