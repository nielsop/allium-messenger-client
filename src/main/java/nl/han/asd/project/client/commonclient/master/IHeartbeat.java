package nl.han.asd.project.client.commonclient.master;

import java.io.IOException;

import nl.han.asd.project.client.commonclient.connection.MessageNotSendException;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientHeartbeat;

/**
 * Interface defining the heartbeat functions.
 *
 * @version 1.0
 */
public interface IHeartbeat {

    /**
     * Send the heartbeat to the master application.
     *
     * @param heartbeat the heartbeat to send to the server
     *
     * @throws IOException if an {@link IOException} occurred
     *          while preparing to send/sending the heartbeat
     *
     * @throws IllegalArgumentException if heartbeat is null
     * @throws MessageNotSendException if the method
     *          was unable to send the message
     */
    void sendHeartbeat(ClientHeartbeat heartbeat) throws IOException, MessageNotSendException;
}
