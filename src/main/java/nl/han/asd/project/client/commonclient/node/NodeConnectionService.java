package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;

public class NodeConnectionService implements ISetConnectedNodes {
    private INodeConnection nodeConnection;

    @Inject
    public NodeConnectionService(INodeConnection nodeConnection) {
        this.nodeConnection = nodeConnection;
    }
}
