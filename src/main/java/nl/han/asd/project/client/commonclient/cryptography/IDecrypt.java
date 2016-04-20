package nl.han.asd.project.client.commonclient.cryptography;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Created by Niels Bokmans on 12-4-2016.
 */
public interface IDecrypt {
    public HanRoutingProtocol.Message decryptEncryptedMessage(EncryptedMessage encryptedMessage);
}
