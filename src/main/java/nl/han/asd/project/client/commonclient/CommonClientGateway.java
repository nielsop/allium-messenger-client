package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.*;
import nl.han.asd.project.client.commonclient.utility.ICommonClientGateway;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Android/Desktop application
 * <p/>
 * Leave empty until we know what to do with it
 */
public class CommonClientGateway implements ICommonClientGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageStoreObserver messageStoreObserver;
    private IRegistration registration;
    private ILoginService login;
    private String privateKey = "privateKey";
    private byte[] publicKey = "publicKey".getBytes();
    private String secretHash = "secretHash";

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IMessageBuilder messageBuilder, IMessageStoreObserver messageStoreObserver,
            IRegistration registration, ILoginService login) {
        this.contactStore = contactStore;
        this.messageStore = messageStore;
        this.messageBuilder = messageBuilder;
        this.messageStoreObserver = messageStoreObserver;
        this.registration = registration;
        this.login = login;

        // TODO remove test method
        createTestContacts();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    // TODO remove test method
    private void createTestContacts() {
        contactStore.createTestContacts();
    }

    @Override
    public HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password, String passwordRepeat) {
        RegisterResponseWrapper registerResponse = registration.register(username, password, passwordRepeat);
        switch (registerResponse.getStatus()) {
            case SUCCES:
                break;
            case FAILED:
                break;
            case TAKEN_USERNAME:
                break;
            default:
                break;
        }
        return registerResponse.getStatus();
    }

    @Override
    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new CurrentUser(username, publicKey, secretHash));
        }
        return loginResponse.getStatus();
    }

    @Override
    public List<Message> getMessagesFromUser(String contact) {
        return messageStore.getMessagesFromUser(contact);
    }

    @Override
    public CurrentUser getCurrentUser() {
        return contactStore.getCurrentUser();
    }

    @Override
    public List<Contact> getContacts() {
        return contactStore.getAllContacts();
    }

    @Override
    public void addMessage(Message message) {
        messageStore.addMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
        //TODO: Actually send message to a user
        LOGGER.info(message.getSender().getUsername() + " sends the following massage: " + message.getText());
        messageStore.addMessage(message);
    }

    @Override
    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    @Override
    public void logout() {
        contactStore.deleteAllContacts();
        messageStore.saveToDatabase();
        messageStore.clear();
        LOGGER.info("Logout successful: " + login.logout(contactStore.getCurrentUser().getCurrentUserAsContact().getUsername(), contactStore.getCurrentUser().getSecretHash()));
    }
}
