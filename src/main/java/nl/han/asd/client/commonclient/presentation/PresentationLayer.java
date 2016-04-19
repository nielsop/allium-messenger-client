package nl.han.asd.client.commonclient.presentation;


import nl.han.asd.client.commonclient.message.IMessageBuilder;
import nl.han.asd.client.commonclient.registration.IRegistration;
import nl.han.asd.client.commonclient.store.IContact;
import nl.han.asd.project.protocol.HanRoutingProtocol;

/**
 * Android/Desktop application
 * <p>
 * Leave empty until we know what to do with it
 */
public class PresentationLayer {
    //TODO: android app? desktop app?
    public IContact contact;
    public IMessageBuilder messageBuilder;
    public IMessageObserver messageObserver;
    public IRegistration registration;
    //public ILogin login; //There is no ILogin yet.

    /**
     * Constructs a presentation layer, using one given gateway.
     * @param registration
     */
    public PresentationLayer(IRegistration registration){
        this.registration = registration;
    }

    /**
     * Register a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     * @param username username given by user.
     * @param password password given by user.
     */
    public HanRoutingProtocol.ClientRegisterResponse.Status register(String username, String password) {
        //Get registering response
        HanRoutingProtocol.ClientRegisterResponse registerResponse = registration.register(username, password);
        //In every other case, do something.
        switch(registerResponse.getStatus()) {
            case SUCCES:
                System.out.println("Registering worked!");
                break;
            case FAILED:
                System.out.println("Registering failed!");
                break;
            case TAKEN_USERNAME:
                System.out.println("Username already exists, registering failed.");
                break;
        }
        //Return the status
        return registerResponse.getStatus();
    }
}
