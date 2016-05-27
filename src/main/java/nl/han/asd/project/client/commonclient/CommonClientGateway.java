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
public class CommonClientGateway implements ICommonClient {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageStoreObserver messageStoreObserver;
    private IRegistration registration;
    private ILogin login;
    private String privateKey = "privateKey";
    private String publicKey = "publicKey";

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IMessageBuilder messageBuilder, IMessageStoreObserver messageStoreObserver,
            IRegistration registration, ILogin login) {
        this.contactStore = contactStore;
        this.messageStore = messageStore;
        this.messageBuilder = messageBuilder;
        this.messageStoreObserver = messageStoreObserver;
        this.registration = registration;
        this.login = login;
    }

    /**
     * PaneRegister a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username username given by user.
     * @param password password given by user.
     */
    @Override
    public HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password, String passwordRepeat) throws IllegalArgumentException {
        //Get registering response
        RegisterResponseWrapper registerResponse = registration.register(username, password, passwordRepeat);
        //In every other case, do something.
        switch (registerResponse.getStatus()) {
            case SUCCES:
                LOGGER.info("Registering worked!");
                break;
            case FAILED:
                LOGGER.info("Registering failed!");
                break;
            case TAKEN_USERNAME:
                LOGGER.info("Username already exists, registering failed.");
                break;
            default:
                LOGGER.info("Something went wrong (default).");
                break;
        }
        //Return the status
        return registerResponse.getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) throws IllegalArgumentException {
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info("User: \"" + username + "\" loginRequest status: " + loginResponse.getStatus().name());
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new CurrentUser(username, publicKey.getBytes(), loginResponse.getSecretHash()));
        }
        return loginResponse.getStatus();
    }

    // Contact functions

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getCurrentUser() {
        LOGGER.info("Find the current user");
        return contactStore.getCurrentUserAsContact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getContacts() {
        LOGGER.info("Find all contacts");
        return contactStore.getAllContacts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(String username) {
        contactStore.addContact(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact findContact(String username) {
        return contactStore.findContact(username);
    }

    // Message functions

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getMessagesFromUser(String contact) {
        LOGGER.info("Find messages from user: " + contact);
        return messageStore.getMessagesFromUser(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addMessage(Message message) {
        messageStore.addMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Message message) {
        //TODO: Actually send message to a user
        LOGGER.info(message.getSender().getUsername() + " sends the following massage: " + message.getText());
        messageStore.addMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() {
        //TODO: Implement method. Delete all in memory user data.
    }
}
