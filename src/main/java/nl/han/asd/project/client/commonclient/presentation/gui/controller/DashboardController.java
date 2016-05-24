package nl.han.asd.project.client.commonclient.presentation.gui.controller;

import javafx.scene.layout.BorderPane;
import nl.han.asd.project.client.commonclient.message.Message;
import nl.han.asd.project.client.commonclient.presentation.gui.GUI;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard.ChatController;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard.ContactController;
import nl.han.asd.project.client.commonclient.presentation.gui.controller.dashboard.NavController;
import nl.han.asd.project.client.commonclient.presentation.gui.model.DashboardModel;
import nl.han.asd.project.client.commonclient.presentation.gui.view.DashboardView;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

/**
 * Created by Marius on 25-04-16.
 */
public class DashboardController {
    private DashboardModel model;
    private DashboardView view;

    private NavController navController;
    private ContactController contactController;
    private ChatController chatController;

    public DashboardController(GUI gui) {
        model = new DashboardModel(gui);
        view = new DashboardView();

        navController = new NavController(this);
        contactController = new ContactController(this);
        chatController = new ChatController(this);

        view.setBorderPaneContent(navController.getHBox(), contactController.getBorderPane(), chatController.getBorderPane());
    }


    public void selectContact(Contact contact) {
//        paneChat.setContact(contactStore);
    }

    public ContactController getContactController() {
        return contactController;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public BorderPane getBorderPane() {
        return view.getBorderPane();
    }

    public Contact getMe() {
        return model.getCurrentUser();
    }

    public ArrayList<Contact> getContacts() {
        return model.getContacts();
    }

    public void sendMessage(Message message) {
        model.sendMessage(message);
    }

    public GUI getGUI() {
        return model.getGUI();
    }
}
