package nl.han.asd.project.client.commonclient.presentation.gui.model;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

/**
 * Created by Marius on 25-04-16.
 */
public class DashboardModel {
    private GUI gui;
    private Contact me;
    private ArrayList<Contact> contacts;

    public DashboardModel(GUI gui) {
        this.gui = gui;
        me = gui.pLayer.getCurrentUser();
    }

    public GUI getGUI() {
        return gui;
    }

    public Contact getMe() {
        return me;
    }

    public ArrayList<Message> getMessages(Contact contact) {
//        return gui.pLayer.getMessages(contact);
        return null;
    }

    public ArrayList<Contact> getContacts() {
//        return gui.pLayer.getContacts();
        return null;
    }

    public void sendMessage(Message message) {
//        gui.pLayer.sendMessage(message);
    }
}
