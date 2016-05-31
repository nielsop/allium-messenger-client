package nl.han.asd.project.client.commonclient.utility;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.store.Contact;
import nl.han.asd.project.client.commonclient.store.CurrentUser;
import nl.han.asd.project.protocol.HanRoutingProtocol;

import java.util.List;

public interface ICommonClientGateway {

    /**
     * Register a user on the master application with the given credentials.
     * Use the MasterGateway to register a user.
     *
     * @param username       username given by user.
     * @param password       password given by user.
     * @param passwordRepeat repeated password given by the user.
     * @return RegisterResponse.status
     * @throws IllegalArgumentException
     */
    HanRoutingProtocol.ClientRegisterResponse.Status registerRequest(String username, String password, String passwordRepeat);

    HanRoutingProtocol.ClientLoginResponse.Status loginRequest(String username, String password);

    List<Message> getMessagesFromUser(String contact);

    CurrentUser getCurrentUser();

    List<Contact> getContacts();

    void addMessage(Message message);

    void sendMessage(Message message);

    void removeContact(String username);

    void logout();
}
