package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.IReceiveMessage;

import javax.inject.Inject;

/**
 * Created by Marius on 19-04-16.
 */
public class NodeConnectionService implements ISetConnectedNodes {
    private IReceiveMessage receiveMessage;
    private INodeConnection nodeConnection;

    //TODO: fix Dependency Injection
    /*@Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, INodeConnection nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }*/
}
