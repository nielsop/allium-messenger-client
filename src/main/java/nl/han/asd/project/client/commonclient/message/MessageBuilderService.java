package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

public class MessageBuilderService implements IMessageBuilder {

    private int MIN_HOPS = 3;
    private IGetPath pathDeterminationService;

    public MessageBuilderService(IGetPath pathDeterminationService) {
        this.pathDeterminationService = pathDeterminationService;
    }

    public EncryptedMessage buildMessage(String messageText, Contact contactOntvanger) {
        ArrayList<Node> path = pathDeterminationService.getPath(MIN_HOPS,contactOntvanger);
        Message message = new Message(messageText,contactOntvanger.getUsername());

        return null;
    }

    private void encryptMessage(ArrayList<Node> path,String publicKey,Message message){
        // loop over path
        // call cryptographyService to encrypt

    }

}
