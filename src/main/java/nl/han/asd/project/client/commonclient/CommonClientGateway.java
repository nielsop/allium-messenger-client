package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.IMessageConfirmation;
import nl.han.asd.project.client.commonclient.message.ISendMessage;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStore;
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterRequest;
import nl.han.asd.project.protocol.HanRoutingProtocol.ClientRegisterResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static nl.han.asd.project.protocol.HanRoutingProtocol.*;
import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse;

/**
 * Android/Desktop application
 * <p/>
 * Leave empty until we know what to do with it
 */
public class CommonClientGateway {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);
    private final IMessageConfirmation messageConfirmation;

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService loginService;
    private ISendMessage sendMessage;
    private IMessageBuilder messageBuilder;

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IRegistration registration,
                               ILoginService loginService, ISendMessage sendMessage, IMessageBuilder messageBuilder,
                               IMessageConfirmation messageConfirmation) {
        this.messageConfirmation = Check.notNull(messageConfirmation, "messageConfirmation");
        this.messageBuilder = Check.notNull(messageBuilder, "messageBuilder");
        this.sendMessage = Check.notNull(sendMessage, "sendMessage");
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
        this.registration = Check.notNull(registration, "registration");
        this.loginService = Check.notNull(loginService, "loginService");

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

    public ClientLoginResponse.Status loginRequest(String username, String password) throws InvalidCredentialsException, IOException, MessageNotSentException {
        try {
            contactStore.setCurrentUser(loginService.login(username, password));
            return ClientLoginResponse.Status.SUCCES;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
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

    public void sendMessage(Message message, Contact contact) {
        HanRoutingProtocol.Message.Builder builder = HanRoutingProtocol.Message.newBuilder();
        builder.setId("123");
        builder.setSender(getCurrentUser().getCurrentUserAsContact().getUsername());
        builder.setText(message.getText());
        builder.setTimeSent(System.currentTimeMillis() / 1000L);

        MessageWrapper messageWrapper = messageBuilder.buildMessage(builder.build(), contact);

        sendMessage.sendMessage(messageWrapper);
        messageConfirmation.messageSent(builder.getId(), message, contact);
        messageStore.addMessage(message);
    }

    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    public void logout() {

    }
}
