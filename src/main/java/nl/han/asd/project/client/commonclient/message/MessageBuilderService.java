package nl.han.asd.project.client.commonclient.message;

import com.google.protobuf.ByteString;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.ArrayList;

public class MessageBuilderService implements IMessageBuilder {

    private int MIN_HOPS = 3;
    private IGetPath pathDeterminationService;
    private IEncrypt cryptographyService;


    public MessageBuilderService(IGetPath pathDeterminationService, IEncrypt cryptographyService) {
        this.pathDeterminationService = pathDeterminationService;
        this.cryptographyService = cryptographyService;
    }

    public void buildMessagePackage(String messageText, Contact contactReciever, Contact contactSender) {
        ArrayList<Node> path = pathDeterminationService.getPath(MIN_HOPS,contactReciever);
        //build protocol message
        //encrypt protocol message
        Message message = new Message(messageText,contactSender,contactReciever);
        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0),message);
        path.remove(0);
        recursiveEncrypt(firstLayer,path);
    }

    private ByteString buildFirstMessagePackageLayer(Node node,Message message) {
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setUsername(message.getReciever().getUsername());
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(cryptographyService.encryptData(message.getText(),message.getReciever().getPublicKey()));

        return builder.build().toByteString();
    }

    private ByteString recursiveEncrypt(ByteString message, ArrayList<Node> path){
        if (path.size() == 0) {
            return message;
        }
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        Node node = path.get(0);
        builder.setIPaddress(node.getIP());
        builder.setPort(node.getPort());
        builder.setEncryptedData(message);

        path.remove(0);

        ByteString encryptedMessage = builder.build().toByteString();

        return recursiveEncrypt(encryptedMessage, path);
    }

}
