package nl.han.asd.project.client.commonclient.master;

import java.io.IOException;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.GraphUpdateResponse;

/**
 * Interface defining the graph update methods.
 *
 * @version 1.0
 */
public interface IGetGraphUpdates {

    /**
     * Send the graph update request to the server
     * returning the received response.
     *
     * @param request the request to be send to the master
     *          application
     *
     * @return the response received from the server
     *
     * @throws IllegalArgumentException if request is null
     * @throws IOException if the function was unable to send
     *          the wrapper due to a socket related
     *          exception
     * @throws MessageNotSendException if the connection service
     *          was unable to send the message. Note that
     *          this exception is not thrown on Socket related
     *          exceptions. See IOException.
     */
    public GraphUpdateResponse getUpdatedGraph(GraphUpdateRequest request) throws IOException, MessageNotSentException;
}
