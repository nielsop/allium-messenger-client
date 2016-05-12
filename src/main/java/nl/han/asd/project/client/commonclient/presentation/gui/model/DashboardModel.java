package nl.han.asd.project.client.commonclient.presentation.gui.model;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.List;

/**
 * Created by Marius on 25-04-16.
 */
public class DashboardModel {
    private GUI gui;
    private Contact currentUser;

    public DashboardModel(GUI gui) {
        this.gui = gui;
        currentUser = getGUI().getPresentationLayer().getCurrentUser();
    }

    public GUI getGUI() {
        return gui;
    }

    public Contact getCurrentUser() {
        return currentUser;
    }

    public List<Message> getMessages(Contact contact) {
        return getGUI().getPresentationLayer().getMessages(contact);
    }

    public List<Contact> getContacts() {
        return getGUI().getPresentationLayer().getContacts();
    }

    public void sendMessage(Message message) {
        getGUI().getPresentationLayer().sendMessage(message);
    }
}
