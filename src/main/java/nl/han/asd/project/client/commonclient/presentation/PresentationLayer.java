package nl.han.asd.project.client.commonclient.presentation;

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
import nl.han.asd.project.client.commonclient.utility.Validation;
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
public class PresentationLayer {

    public static final Logger LOGGER = LoggerFactory.getLogger(PresentationLayer.class);

    private IContactStore contacts;
    private IMessageStore messages;
    private IMessageBuilder messageBuilder;
    private IMessageObserver messageObserver;
    private IRegistration registration;
    private ILogin login;
    private Contact currentUser;
    private String privateKey = "privateKey";

    @Inject
    public PresentationLayer(IContactStore contact, IMessageBuilder messageBuilder, IMessageObserver messageObserver, IRegistration registration, ILogin login) {
        this.contacts = contact;
        this.messageBuilder = messageBuilder;
        this.messageObserver = messageObserver;
        this.registration = registration;
        this.login = login;

        // TODO remove test method
        createTestContacts();
    }

    // TODO remove test method
    private void createTestContacts() {
        contacts.createTestContacts();
    }

    /**
     * PaneRegister a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username username given by user.
     * @param password password given by user.
     */
    //TODO: Use validation in a better way?
    public HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password) {
        Validation.validateUserAndPass(username, password);
        //Get registering response
        RegisterResponseWrapper registerResponse = registration.register(username, password);
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

    public Contact getCurrentUser() {
        return currentUser;
    }

    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) {
        Validation.validateUserAndPass(username, password);
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info("User: \"" + username + "\" loginRequest status: " + loginResponse.getStatus().name());
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            currentUser = new Contact(username, privateKey, true);
        }
        return loginResponse.getStatus();
    }

    public List<Message> getMessages(String contact) {
        LOGGER.info("Find messages for user: " + contact);
        return messages.getMessages(contact);
    }

    public List<Contact> getContacts() {
        LOGGER.info("Find contacts");
        return contacts.getAllContacts();
    }

    public void sendMessage(Message message) {
        LOGGER.info(message.getSender() + " sends to " + message.getReceiver() + "the following massage:\n" + message.getText());
        messages.sendMessage(message);
    }
}
