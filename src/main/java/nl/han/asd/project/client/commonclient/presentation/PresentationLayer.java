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
import nl.han.asd.project.client.commonclient.utility.Validation;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Android/Desktop application
 * <p/>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer {

    public static final Logger LOGGER = LoggerFactory.getLogger(PresentationLayer.class);

    private IContactStore contact;
    private IMessageBuilder messageBuilder;
    private IMessageObserver messageObserver;
    private IRegistration registration;
    private ILogin login;
    private Contact currentUser;
    private String privateKey = "privateKey";

    /**
     * Constructs a presentation layer, using one given gateway.
     *
     * @param registration
     */
    public PresentationLayer(IRegistration registration) {
        this.registration = registration;
    }

    @Inject
    public PresentationLayer(IContactStore contact, IMessageBuilder messageBuilder, IMessageObserver messageObserver, IRegistration registration, ILogin login) {
        this.contact = contact;
        this.messageBuilder = messageBuilder;
        this.messageObserver = messageObserver;
        this.registration = registration;
        this.login = login;

        // TODO remove test method
        setupTestData();
    }

    private void setupTestData() {
        contact.createTestContacts();
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
            default:
                LOGGER.info("Default response. Something went wrong...");
                break;
        }
        //Return the status
        return registerResponse.getStatus();
    }

    public Contact getCurrentUser() {
        return currentUser;
    }

    public HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info("User: \"" + username + "\" loginRequest status: " + loginResponse.getStatus().name());
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            currentUser = new Contact(username, privateKey, true);
        }
        return loginResponse.getStatus();
    }

    public List<Message> getMessages(Contact contact) {
        LOGGER.info("Find messages for user: " + contact.getUsername());
        return new ArrayList<>();
    }

    public List<Contact> getContacts() {
        LOGGER.info("Find contacts");
        return contact.getAllContacts();
    }

    public void sendMessage(Message message) {
        PresentationLayer.LOGGER.info(message.getSender() + " sends to " + message.getReceiver() + "the following massage:\n" + message.getText());
        System.out.println(message.getSender() + " sends to " + message.getReceiver() + "the following massage:\n" + message.getText());
    }
}
