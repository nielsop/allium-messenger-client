package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import com.google.protobuf.GeneratedMessage;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import nl.han.asd.project.client.commonclient.store.Contact;

public class NodeConnectionService implements ISetConnectedNodes, ISendData {
    private IReceiveMessage receiveMessage;
    private IConnectionListener nodeConnection;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage, IConnectionListener nodeConnection) {
        this.receiveMessage = receiveMessage;
        this.nodeConnection = nodeConnection;
    }

    @Override public void sendData(byte[] data, Contact receiver) {

    }
}
