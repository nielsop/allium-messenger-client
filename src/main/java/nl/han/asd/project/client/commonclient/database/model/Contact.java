package nl.han.asd.project.client.commonclient.database.model;

/**
 *
 *
 * @author Niels Bokmans
 * @version 1.0
 * @since 20-5-2016
 */
public class Contact {

    private String username;

    public Contact(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public static Contact fromDatabase(Object contactObj) {
        return new Contact((String) contactObj);
    }

    @Override
    public String toString() {
        return getUsername();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (!(anotherObject instanceof Contact)) {
            return false;
        }
        Contact contact = (Contact) anotherObject;
        return contact.getUsername().equals(getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}
