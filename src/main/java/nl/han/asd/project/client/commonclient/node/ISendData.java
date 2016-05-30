package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.store.Contact;

/**
 * @author Julius
 * @version 1.0
 * @since 24/05/16
 */
@FunctionalInterface
public interface ISendData {
    void sendData(byte[] data, Contact receiver);
}
