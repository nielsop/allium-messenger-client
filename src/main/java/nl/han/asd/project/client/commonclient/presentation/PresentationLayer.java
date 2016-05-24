package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PresentationLayer.class);

    public IContactStore contactStore;
    public IMessageBuilder messageBuilder;
    public IMessageObserver messageObserver;
    public IRegistration registration;
    public ILogin login;
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
    public PresentationLayer(IContactStore contactStore, IMessageBuilder messageBuilder, IMessageObserver messageObserver, IRegistration registration, ILogin login) {
        this.contactStore = contactStore;
        this.messageBuilder = messageBuilder;
        this.messageObserver = messageObserver;
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
    public HanRoutingProtocol.ClientRegisterResponse.Status register(String username, String password) {
        //Get registering response
        RegisterResponseWrapper registerResponse = registration.register(username, password);
        //In every other case, do something.
        switch (registerResponse.status) {
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
        return registerResponse.status;
    }

    public HanRoutingProtocol.ClientLoginResponse.Status login(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info("User: \"" + username + "\" login status: " + loginResponse.status.name());
        if (loginResponse.status == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new CurrentUser(username, privateKey, loginResponse.secretHash));
        }
        return loginResponse.status;
    }

    public Contact getCurrentUser() {
        return contactStore.getCurrentUser();
    }

    // Contact functions
    public void addContact(String username) {
        contactStore.addContact(username);
    }

    public void deleteContact(String username) {
        contactStore.deleteContact(username);
    }

    public void deleteAllContacts() {
        contactStore.deleteAllContacts();
    }

    public List<Contact> getAllContacts() {
        return contactStore.getAllContacts();
    }

    public Contact findContact(String username) {
        return findContact(username);
    }
}