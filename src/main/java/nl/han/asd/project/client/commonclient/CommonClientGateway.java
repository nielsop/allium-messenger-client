package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.*;
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
public class CommonClientGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageStoreObserver messageStoreObserver;
    private IRegistration registration;
    private ILogin login;
    private String privateKey = "privateKey";
    private byte[] publicKey = "publicKey".getBytes();
    private String secretHash = "secretHash";

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IMessageBuilder messageBuilder, IMessageStoreObserver messageStoreObserver, IRegistration registration, ILogin login) {
        this.contactStore = contactStore;
        this.messageStore = messageStore;
        this.messageBuilder = messageBuilder;
        this.messageStoreObserver = messageStoreObserver;
        this.registration = registration;
        this.login = login;

        // TODO remove test method
        createTestContacts();
    }

    // TODO remove test method
    private void createTestContacts() {
        contactStore.createTestContacts();
    }

    /**
     * Register a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username username given by user.
     * @param password password given by user.
     * @param passwordRepeat repeated password given by the user.
     * @return RegisterResponse.status
     * @throws IllegalArgumentException
     */
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

    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new CurrentUser(username, publicKey.getBytes(), secretHash));
        }
        return loginResponse.getStatus();
    }

    public List<Message> getMessagesFromUser(String contact) {
        return messageStore.getMessagesFromUser(contact);
    }

    public CurrentUser getCurrentUser() {
        return contactStore.getCurrentUser();
    }

    public List<Contact> getContacts() {
        return contactStore.getAllContacts();
    }

    public void addMessage(Message message) {
        messageStore.addMessage(message);
    }

    public void sendMessage(Message message) {
        //TODO: Actually send message to a user
        LOGGER.info(message.getSender().getUsername() + " sends the following massage: " + message.getText());
        messageStore.addMessage(message);
    }

    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    public void logout() {
        throw new UnsupportedOperationException();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
