package nl.han.asd.project.client.commonclient.presentation;

import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.node.ISendMessage;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.client.commonclient.store.IContactStore;
import nl.han.asd.project.client.commonclient.store.IMessageStoreObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class CommonClientGateway implements ICommonClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonClientGateway.class);

    public IContactStore contactStore;
    public IMessageBuilder messageBuilder;
    public IMessageStoreObserver messageObserver;
    public IRegistration registration;
    public ILogin login;
    private ISendMessage sendMessage;
    private String privateKey = "privateKey";

    /**
     * Constructs a presentation layer, using one given gateway.
     *
     * @param registration
     */
    public CommonClientGateway(IRegistration registration) {
        this.registration = registration;
    }

    @Inject
    public CommonClientGateway(IContactStore contactStore, IMessageBuilder messageBuilder,
            IMessageStoreObserver messageObserver, IRegistration registration, ILogin login, ISendMessage sendMessage) {
        this.contactStore = contactStore;
        this.messageBuilder = messageBuilder;
        this.messageObserver = messageObserver;
        this.registration = registration;
        this.login = login;
        this.sendMessage = sendMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HanRoutingProtocol.ClientRegisterResponse.Status register(String username, String password) {
        RegisterResponseWrapper registerResponse = registration.register(username, password);
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
        return registerResponse.getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HanRoutingProtocol.ClientLoginResponse.Status login(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);
        LOGGER.info(String.format("User '%S' has login status '%S'.", username, loginResponse.getStatus().name()));
        if (loginResponse.getStatus() == HanRoutingProtocol.ClientLoginResponse.Status.SUCCES) {
            contactStore.setCurrentUser(new CurrentUser(username, privateKey, loginResponse.getSecretHash()));
        }
        return loginResponse.getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getCurrentUser() {
        return contactStore.getCurrentUserAsContact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getContacts() throws SQLException {
        return contactStore.getAllContacts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeContact(String username) throws SQLException {
        contactStore.removeContact(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addContact(String username) throws SQLException {
        contactStore.addContact(username);
    }

    //TODO: Implement method. Delete all in memory user data.
    /**
     * {@inheritDoc}
     */
    @Override
    public void logout() {
    }
}
