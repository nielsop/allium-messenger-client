package nl.han.asd.project.client.commonclient.message;

import nl.han.asd.project.client.commonclient.node.Node;
import nl.han.asd.project.client.commonclient.path.IGetPath;
import nl.han.asd.project.client.commonclient.store.Contact;

public class MessageBuilderService implements IMessageBuilder {

    private IGetPath pathDeterminationService;
    public MessageBuilderService(IGetPath pathDeterminationService) {
        this.pathDeterminationService = pathDeterminationService;
    }

    public EncryptedMessage buildMessage(String message, Contact contactOntvanger) {
        Node[] path = pathDeterminationService.getPath(3,contactOntvanger);
        return null;
    }
}
