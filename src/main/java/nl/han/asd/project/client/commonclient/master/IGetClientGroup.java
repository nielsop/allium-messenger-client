package nl.han.asd.project.client.commonclient.master;

import java.io.IOException;

import nl.han.asd.project.client.commonclient.connection.MessageNotSendException;
import nl.han.asd.project.protocol.HanRoutingProtocol.Client;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRequest;

/**
 * Define the client group methods.
 *
 * @version 1.0
 */
public interface IGetClientGroup {

    /**
     * Send the client group request to the
     * server returning the received response.
     *
     * @param request the request to be send to the master application
     *
     * @return the response received from the server
     *
     * @throws IllegalArgumentException if request is null
     * @throws IOException if an {@link IOException} occurred
     *          while preparing to send/sending the request
     * @throws MessageNotSendException
     */
    public Client getClientGroup(ClientRequest request) throws IOException, MessageNotSendException;
}
