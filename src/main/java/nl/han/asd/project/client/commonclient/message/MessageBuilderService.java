package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;

import javax.inject.Inject;


public class MessageBuilderService implements IMessageBuilder {
    public IGetPath getPath;
    public ISendMessage sendMessage;
    public IMessageStore messageStore;
    public IEncrypt encrypt;
    public IDecrypt decrypt;

    @Inject
    public MessageBuilderService(IGetPath getPath, ISendMessage sendMessage, IMessageStore messageStore) {
        this.getPath = getPath;
        this.sendMessage = sendMessage;
        this.messageStore = messageStore;
    }

    private int MIN_HOPS = 3;
    private IGetPath pathDeterminationService;
    private IEncrypt cryptographyService;


    public MessageBuilderService(IGetPath pathDeterminationService, IEncrypt cryptographyService) {
        this.pathDeterminationService = pathDeterminationService;
        this.cryptographyService = cryptographyService;
    }

    /*

    public void buildMessagePackage(String messageText, Contact contactReciever, Contact contactSender) {
        ArrayList<Node> path = pathDeterminationService.getPath(MIN_HOPS,contactReciever);
        //build protocol message
        //encrypt protocol message
        nl.han.asd.project.client.commonclient.message.Message message = new nl.han.asd.project.client.commonclient.message.Message(messageText,contactSender,contactReciever);
        ByteString firstLayer = buildFirstMessagePackageLayer(path.get(0),message);
        path.remove(0);
        recursiveEncrypt(firstLayer,path);
    }

    private ByteString buildFirstMessagePackageLayer(Node node, nl.han.asd.project.client.commonclient.message.Message message) {
        HanRoutingProtocol.EncryptedMessage.Builder builder = HanRoutingProtocol.EncryptedMessage.newBuilder();

        builder.setUsername(message.getService().getUsername());
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
    }*/
}
