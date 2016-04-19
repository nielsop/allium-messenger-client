package nl.han.asd.client.commonclient.node;

import nl.han.asd.client.commonclient.message.IReceiveMessage;

import javax.inject.Inject;

/**
 * Created by Marius on 19-04-16.
 */
public class NodeConnectionService implements ISetConnectedNodes {
    private IReceiveMessage receiveMessage;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage) {
        this.receiveMessage = receiveMessage;
    }
}
