package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * @author Julius
 * @version 1.0
 * @since 24/05/16
 */
public interface ISendData {

    /**
     * Send a MessageWrapper to a Node.
     *
     * @param messageWrapper The wrapper to be sent
     *
     * @throws MessageNotSentException
     */
    void sendData(HanRoutingProtocol.MessageWrapper messageWrapper) throws MessageNotSentException;
}
