package nl.han.asd.project.client.commonclient.connection;

import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;

/**
 * Created by Jevgeni on 18-5-2016.
 */
public interface IConnectionServiceFactory {
    ConnectionService create(byte[] receiverPublicKey);
}
