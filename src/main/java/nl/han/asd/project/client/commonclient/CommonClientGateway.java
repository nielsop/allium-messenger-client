package nl.han.asd.project.client.commonclient;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.store.IMessageStoreObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;

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
    private IMessageStoreObserver messageStoreObserver;
    private IRegistration registration;
    private ILogin login;
    private String privateKey = "privateKey";
    private byte[] publicKey = "publicKey".getBytes();
    private String secretHash = "secretHash";

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IMessageBuilder messageBuilder,
            IMessageStoreObserver messageStoreObserver, IRegistration registration, ILogin login) {
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
     *
     * @return RegisterResponse.status
     *
     * @throws IllegalArgumentException
     * @throws MessageNotSentException
     * @throws IOException
     */
    public HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password,
            String passwordRepeat) throws IOException, MessageNotSentException {
        if (password.equals(passwordRepeat)) {
            return ClientRegisterResponse.Status.FAILED;
        }

        ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest.newBuilder();
        requestBuilder.setUsername(username);
        requestBuilder.setPassword(password);

        return registration.register(requestBuilder.build()).getStatus();
    }

    public CurrentUser loginRequest(String username, String password)
            throws InvalidCredentialsException, IOException, MessageNotSentException {
        return login.login(username, password);
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
        messageStore.addMessage(message);
    }

    public void addContact(String username) {
        contactStore.addContact(username, username.getBytes()); //TODO: actual public key instead of username.getBytes()
    }

    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    public void logout() {

    }
}
