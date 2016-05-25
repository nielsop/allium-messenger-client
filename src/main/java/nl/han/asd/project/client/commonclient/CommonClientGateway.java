package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IMessageBuilder messageBuilder;
    private IMessageObserver messageObserver;
    private IRegistration registration;
    private ILogin login;
    private String privateKey = "privateKey";

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IMessageBuilder messageBuilder, IMessageObserver messageObserver, IRegistration registration, ILogin login) {
        this.contactStore = contactStore;
        this.messageStore = messageStore;
        this.messageBuilder = messageBuilder;
        this.messageObserver = messageObserver;
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
     * PaneRegister a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username username given by user.
     * @param password password given by user.
     */
    public HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password, String passwordRepeat) throws IllegalArgumentException  {
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
        }
        //Return the status
        return registerResponse.getStatus();
    }

    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) throws IllegalArgumentException  {
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info("User: \"" + username + "\" loginRequest status: " + loginResponse.getStatus().name());
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new Contact(username, privateKey, true));
        }
        return loginResponse.getStatus();
    }

    public List<Message> getMessages(String contact) {
        LOGGER.info("Find messages from user: " + contact);
        return messageStore.getMessages(contact);
    }

    public Contact getCurrentUser() {
        LOGGER.info("Find the current user");
        return contactStore.getCurrentUser();
    }

    public List<Contact> getContacts() {
        LOGGER.info("Find all contacts");
        return contactStore.getAllContacts();
    }

    public void addMessage(Message message) {
        messageStore.addMessage(message);
    }

    public void sendMessage(Message message) {
        LOGGER.info(message.getSender().getUsername() + " sends to " + message.getReceiver().getUsername() + " the following massage: " + message.getText());
        messageStore.addMessage(message);
    }

    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    public void logout() {

    }
}
