package nl.han.asd.project.client.commonclient.database.model;

public class Contact {

    private String username;

    public Contact(String username) {
        this.username = username;
    }

    public static Contact fromDatabase(Object contactObj) {
        return new Contact((String) contactObj);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return getUsername();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (anotherObject == null || !(anotherObject instanceof Contact)) {
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
