package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContact;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer {

    private static final Logger logger = LoggerFactory.getLogger(PresentationLayer.class);

    //TODO: android app? desktop app?
    public IContact contact;
    public IMessageBuilder messageBuilder;
    public IMessageObserver messageObserver;
    public IRegistration registration;
    public ILogin login;
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
    public PresentationLayer(IContact contact, IMessageBuilder messageBuilder, IMessageObserver messageObserver,
            IRegistration registration, ILogin login) {
        this.contact = contact;
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
                logger.info("Registering worked!");
                break;
            case FAILED:
                logger.info("Registering failed!");
                break;
            case TAKEN_USERNAME:
                logger.info("Username already exists, registering failed.");
                break;
            default:
                logger.info("Default response. Something went wrong...");
                break;
        }
        //Return the status
        return registerResponse.status;
    }

    public Contact getCurrentUser() {
        return currentUser;
    }

    public HanRoutingProtocol.ClientLoginResponse.Status login(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        logger.info("User: \"" + username + "\" login status: " + loginResponse.status.name());
        if (loginResponse.status == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            currentUser = new Contact(username, privateKey, true);
        }
        return loginResponse.status;
    }
}
