package nl.han.asd.project.client.commonclient.store;

/**
 * Created by Jevgeni on 12-5-2016.
 */
public interface IContactStore {
    public void addContact(String username, String publicKey);
    public void deleteContact(String username);
    public Contact findContact(String username);
}
