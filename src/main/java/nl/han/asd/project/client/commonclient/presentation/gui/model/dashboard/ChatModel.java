package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.List;

/**
 * Created by Marius on 25-04-16.
 */
public class ChatModel {
    private DashboardController dashboardController;
    private Contact receiver;
    private Contact currentUser;

    public ChatModel(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
        currentUser = dashboardController.getMe();
    }

    public Contact getCurrentUser() {
        return currentUser;
    }

    public Contact getReceiver() {
        return receiver;
    }

    public void setReceiver(Contact receiver) {
        this.receiver = receiver;
    }

    public void sendMessage(Message message) {
        dashboardController.sendMessage(message);
    }

    public List<Message> getMessages(Contact contact) {
        return dashboardController.getMessages(contact);
    }

}
