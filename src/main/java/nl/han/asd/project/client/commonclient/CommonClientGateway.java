package nl.han.asd.project.client.commonclient;

import nl.han.asd.project.client.commonclient.connection.MessageNotSentException;
import nl.han.asd.project.client.commonclient.login.ILoginService;
import nl.han.asd.project.client.commonclient.login.InvalidCredentialsException;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.scripting.IRunningScriptTracker;
import nl.han.asd.project.client.commonclient.store.*;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    private IContactStore contactStore;
    private IMessageStore messageStore;
    private IRegistration registration;
    private ILoginService loginService;
    private IScriptStore scriptStore;
    private IRunningScriptTracker scriptTracker;

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageStore messageStore, IRegistration registration,
            ILoginService loginService, IScriptStore scriptStore, IRunningScriptTracker scriptTracker) {
        this.contactStore = Check.notNull(contactStore, "contactStore");
        this.messageStore = Check.notNull(messageStore, "messageStore");
        this.registration = Check.notNull(registration, "registration");
        this.loginService = Check.notNull(loginService, "loginService");
        this.scriptStore = Check.notNull(scriptStore, "scriptStore");
        this.scriptTracker = Check.notNull(scriptTracker, "scriptTracker");

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

    public void sendMessage(Message message) {
        //TODO: Actually send message to a user
        messageStore.addMessage(message);
    }

    public void removeContact(String username) {
        contactStore.removeContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    public void logout() {

    }

    public String getScriptContent(String scriptName) {
        return scriptStore.getScriptContent(scriptName);
    }

    public List<String> getAllScriptNames() {
        return scriptStore.getAllScriptNames();
    }

    public void stopScript(String scriptName) {
        scriptTracker.stopScript(scriptName);
    }

    public boolean startScript(String scriptName, String scriptContent) {
        return scriptTracker.startScript(scriptName, scriptContent);
    }

    public void addScript(String scriptName, String scriptContent) {
        scriptStore.addScript(scriptName, scriptContent);
    }

    public void updateScript(String scriptName, String scriptContent) {
        scriptStore.updateScript(scriptName, scriptContent);
    }

    public void removeScript(String scriptName) {
        scriptStore.removeScript(scriptName);
    }
}
