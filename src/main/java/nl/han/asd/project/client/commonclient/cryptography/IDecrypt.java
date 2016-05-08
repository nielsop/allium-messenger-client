package nl.han.asd.project.client.commonclient.cryptography;

import com.google.protobuf.ByteString;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-4-2016
 */
public interface IDecrypt {
    public ByteString decryptData(ByteString data);
}
