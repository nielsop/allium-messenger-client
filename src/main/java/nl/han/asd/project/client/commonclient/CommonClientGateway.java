package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.login.MisMatchingException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageReceiver;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.ISubscribeMessageReceiver;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.*;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import nl.han.asd.project.scripting.internal.IScriptInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;
import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLogoutResponse;

/**
 * Android/Desktop application
 * <p/>
 * Leave empty until we know what to do with it
 */
public class CommonClientGateway {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService loginService;
    private IScriptStore scriptStore;

    private ISendMessage sendMessage;
    private ISubscribeMessageReceiver subscribeMessageReceiver;

    private static CommonClientGateway commonClientGateway;

    /**
     * Constructor of the CommonClientGateway
     *
     * Validates that all parameters arent null
     *
     * @param contactStore used to store contacts
     * @param messageStore used to store messages
     * @param registration used for registration
     * @param loginService used to login
     * @param scriptStore used to store scripts
     * @param sendMessage used to send a message
     * @param subscribeMessageReceiver used to handle message listeners.
     */
    @Inject
    public CommonClientGateway(IContactStore contactStore,
            IMessageStore messageStore, IRegistration registration,
            ILoginService loginService, IScriptStore scriptStore,
            ISendMessage sendMessage,
            ISubscribeMessageReceiver subscribeMessageReceiver) {
        this.subscribeMessageReceiver = Check
                .notNull(subscribeMessageReceiver, "subscribeMessageReceiver");
        this.sendMessage = Check.notNull(sendMessage, "sendMessage");
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
        this.registration = Check.notNull(registration, "registration");
        this.loginService = Check.notNull(loginService, "loginService");
        this.scriptStore = Check.notNull(scriptStore, "scriptStore");
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
    public ClientRegisterResponse.Status registerRequest(String username,
            String password, String passwordRepeat)
            throws IOException, MessageNotSentException {
        try {
            Validation.passwordsEqual(password, passwordRepeat);
            Validation.validateCredentials(username, password);

            ClientRegisterRequest.Builder requestBuilder = ClientRegisterRequest
                    .newBuilder();
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
    public ClientLoginResponse.Status loginRequest(String username,
            String password) throws InvalidCredentialsException, IOException,
            MessageNotSentException {
        try {
            return loginService.login(username, password);
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
    public ClientLogoutResponse.Status logout()
            throws MessageNotSentException, IOException, MisMatchingException {
        try {
            CurrentUser user = contactStore.getCurrentUser();
            return loginService
                    .logout(user.getCurrentUserAsContact().getUsername(),
                            user.getSecretHash());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Gets the content of a script.
     *
     * @param scriptName name of the script of which the content will be fetched
     * @return The content of a script
     */
    public String getScriptContent(String scriptName) {
        return scriptStore.getScriptContent(scriptName);
    }

    /**
     * Gets the names of all the users scripts.
     *
     * @return A list containing the names of all the script of the user.
     */
    public List<String> getAllScriptNames() {
        return scriptStore.getAllScriptNames();
    }

    /**
     * Adds a new script into the database
     *
     * @param scriptName The name of the script that will be added
     * @param scriptContent The content of the script that will be added
     */
    public void addScript(String scriptName, String scriptContent) {
        scriptStore.addScript(scriptName, scriptContent);
    }

    /**
     * Updates the content of a script
     *
     * @param scriptName The name of the script that will be updated
     * @param scriptContent The newly entered content for the script that will be updated
     */
    public void updateScript(String scriptName, String scriptContent) {
        scriptStore.updateScript(scriptName, scriptContent);
    }

    /**
     * Removes a script from the database
     *
     * @param scriptName The name of the script that will be removed
     */
    public void removeScript(String scriptName) {
        scriptStore.removeScript(scriptName);
    }

    /**
     * Subscribe to any messages received by MessageProcessingService
     *
     * @param messageReceiver An instance of IMessageReceiver that will be triggered on received messages
     */
    public void subscribeReceivedMessages(IMessageReceiver messageReceiver) {
        subscribeMessageReceiver.subscribe(messageReceiver);
    }

    public boolean sendMessage(String username, String messageText)
    {
        try {
            Contact contact = contactStore.findContact(username);
            Message message = new Message(contactStore.getCurrentUserAsContact(), contact, new Date(), messageText);
            sendMessage.sendMessage(message, contact);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public Message[] getReceivedMessageAfterDate(Date date)
    {
        long dateTime = date.getTime();
        Message[] receivedMessages = messageStore.getMessagesAfterDate(dateTime);

        return receivedMessages;
    }


}
