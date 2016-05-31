package nl.han.asd.project.client.commonclient.node;

import com.google.inject.Inject;
import nl.han.asd.project.client.commonclient.message.IReceiveMessage;
import nl.han.asd.project.commonservices.internal.utility.Check;

public class NodeConnectionService {
    private IReceiveMessage receiveMessage;

    @Inject
    public NodeConnectionService(IReceiveMessage receiveMessage) {
        this.receiveMessage = Check.notNull(receiveMessage, "receiveMessage");
    }
}
