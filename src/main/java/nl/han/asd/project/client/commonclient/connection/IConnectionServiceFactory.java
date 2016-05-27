package nl.han.asd.project.client.commonclient.connection;


/**
 * Created by Jevgeni on 18-5-2016.
 */
public interface IConnectionServiceFactory {
    ConnectionService create(byte[] receiverPublicKey);
}
