package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;

public class NodeGateway implements ISendMessage {
    @Override
    public void sendMessage(EncryptedMessage message) {
        HanRoutingProtocol.MessageWrapper.Builder request = HanRoutingProtocol.MessageWrapper.newBuilder();

        request.setIPaddress(message.getIp());
        request.setPort(message.getPort());
        request.setData(message.getEncryptedData());
    }
}
