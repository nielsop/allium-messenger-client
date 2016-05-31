package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.protocol.HanRoutingProtocol.Wrapper;

public interface ISendMessage {
    void sendMessage(Wrapper message);
}
