package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;

/**
 * Android/Desktop application
 * <p/>
 * Leave empty until we know what to do with it
 */
public class CommonClientGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService loginService;
    private ISendMessage sendMessage;

    private static CommonClientGateway commonClientGateway;

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IRegistration registration,
                               ILoginService loginService, ISendMessage sendMessage) {
        this.sendMessage = Check.notNull(sendMessage, "sendMessage");
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
        this.registration = Check.notNull(registration, "registration");
        this.loginService = Check.notNull(loginService, "loginService");
    }

    /**
     * Register a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username       username given by user.
     * @param password       password given by user.
     * @param passwordRepeat repeated password given by the user.
     * @return RegisterResponse.status
     * @throws IllegalArgumentException
     * @throws MessageNotSentException
     * @throws IOException
     */
    public ClientRegisterResponse.Status registerRequest(String username, String password, String passwordRepeat) throws IOException, MessageNotSentException {
        try {
            Validation.passwordsEqual(password, passwordRepeat);
            Validation.validateCredentials(username, password);

            ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest.newBuilder();
            requestBuilder.setUsername(username);
            requestBuilder.setPassword(password);

            return registration.register(requestBuilder.build()).getStatus();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Logs in and returns a login status. This login status is wrapped inside a loginResponseWrapper.
     *
     * @param username the username to log in.
     * @param password the password belonging to the username.
     * @return the login status, received from the loginResponseWrapper.
     */
    public ClientLoginResponse.Status loginRequest(String username, String password) throws InvalidCredentialsException, IOException, MessageNotSentException {
        try {
            loginService.login(username, password);
            return ClientLoginResponse.Status.SUCCES;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves messages of current user from messageStore.
     *
     * @param contact the username of the contact
     * @return List with all the messages from/to the given contact
     */
    public List<Message> getMessagesFromUser(String contact) {
        return messageStore.getMessagesFromUser(contact);
    }

    /**
     * Returns the current user that is logged in.
     *
     * @return the current user
     */
    public CurrentUser getCurrentUser() {
        return contactStore.getCurrentUser();
    }

    /**
     * Returns a list of contacts of the current user.
     *
     * @return list of contacts of the current user
     */
    public List<Contact> getContacts() {
        return contactStore.getAllContacts();
    }

    /**
     * Finds contact from username in contactstore.
     *
     * @param username the username of the contact
     * @return found contact by username
     */
    public Contact findContact(String username) {
        return contactStore.findContact(username);
    }

    /**
     * Adds the message to the messageStore.
     *
     * @param message the message to be added
     */
    public void addMessage(Message message) {
        messageStore.addMessage(message);
    }

    /**
     * Sends the message to the receiver by onion routing.
     *
     * @param message the to be send message
     */
    public void sendMessage(Message message) {
        sendMessage.sendMessage(message, message.getReceiver());
    }

    /**
     * Adds contact to contactstore.
     *
     * @param username username of contact
     */
    public void addContact(String username) {
        contactStore.addContact(username);
    }

    /**
     * Removes contact from contactstore.
     *
     * @param username username of contact
     */
    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    /**
     * Logs out the user and deletes all user data in memory
     */
    public void logout() {
        //TODO: Implement method. Delete all in memory user data.
    }
}
