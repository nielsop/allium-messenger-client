package nl.han.asd.project.client.commonclient.node;

import nl.han.asd.project.client.commonclient.message.EncryptedMessage;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeGateway implements ISendMessage {
    private static final Logger logger = LoggerFactory.getLogger(NodeGateway.class);

    @Override
    public void sendMessage(EncryptedMessage message) {
        HanRoutingProtocol.MessageWrapper.Builder request = HanRoutingProtocol.MessageWrapper.newBuilder();

        request.setIPaddress(message.getIp());
        request.setPort(message.getPort());
        request.setEncryptedData(message.getEncryptedData());
    }
}
