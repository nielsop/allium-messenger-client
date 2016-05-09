package nl.han.asd.project.client.commonclient.presentation;


import nl.han.asd.project.client.commonclient.login.ILogin;
import nl.han.asd.project.client.commonclient.master.IRegistration;
import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;
import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;
import nl.han.asd.project.client.commonclient.message.IMessageBuilder;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.IContact;
import nl.han.asd.project.client.commonclient.store.IMessageObserver;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import javax.inject.Inject;
import java.util.ArrayList;

import static nl.han.asd.project.protocol.HanRoutingProtocol.ClientLoginResponse.Status.SUCCES;

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
    public ILogin login;
    private Contact currentUser;

    /**
     * Constructs a presentation layer, using one given gateway.
     *
     * @param registration
     */
    public PresentationLayer(IRegistration registration) {
        this.registration = registration;
    }

    @Inject
    public PresentationLayer(IContact contact, IMessageBuilder messageBuilder, IMessageObserver messageObserver, IRegistration registration, ILogin login) {
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
        return registerResponse.status;
    }

    public Contact getCurrentUser() {
        if (currentUser == null) currentUser = new Contact("Marius", "asdf4321", true);
        return currentUser;
    }

    // TODO testdata, remove when done
    ArrayList<Message> bram = new ArrayList<>();
    ArrayList<Message> niels = new ArrayList<>();
    ArrayList<Message> jevgenie = new ArrayList<>();
    ArrayList<Message> dennis = new ArrayList<>();
    ArrayList<Message> kenny = new ArrayList<>();
    ArrayList<Message> julius = new ArrayList<>();

    public ArrayList<Message> getMessages(Contact contact) {
        if (contact.getUsername().equals("Bram"))  return bram;
        else if (contact.getUsername().equals("Niels")) return niels;
        else if (contact.getUsername().equals("Jevgenie")) return jevgenie;
        else if (contact.getUsername().equals("Dennis")) return dennis;
        else if (contact.getUsername().equals("Kenny")) return kenny;
        else if (contact.getUsername().equals("Julius")) return julius;
        return null;
    }

    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Bram", "asdf4321", true));
        contacts.add(new Contact("Niels", "asdf4321", false));
        contacts.add(new Contact("Jevgenie", "asdf4321", true));
        contacts.add(new Contact("Dennis", "asdf4321", true));
        contacts.add(new Contact("Kenny", "asdf4321", false));
        contacts.add(new Contact("Julius", "asdf4321", false));
        return contacts;
    }

    public void sendMessage(Message message) {
        addMessageToStore(message);
    }

    private void addMessageToStore(Message message) {
        if (message.getReceiver().getUsername().equals("Bram")) bram.add(message);
        else if (message.getReceiver().getUsername().equals("Niels")) niels.add(message);
        else if (message.getReceiver().getUsername().equals("Jevgenie")) jevgenie.add(message);
        else if (message.getReceiver().getUsername().equals("Dennis")) dennis.add(message);
        else if (message.getReceiver().getUsername().equals("Kenny")) kenny.add(message);
        else if (message.getReceiver().getUsername().equals("Julius")) julius.add(message);
    }

    public HanRoutingProtocol.ClientLoginResponse.Status login(String username, String password) {
        LoginResponseWrapper loginResponse = login.login(username, password);

        if (loginResponse.status == SUCCES) {

        }
        return loginResponse.status;
    }
}
