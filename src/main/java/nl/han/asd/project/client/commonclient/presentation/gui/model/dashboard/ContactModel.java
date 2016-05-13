package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.List;

/**
 * Created by Marius on 25-04-16.
 */
public class ContactModel {
    private DashboardController dashboardController;
    private Contact selectedContact;

    public ContactModel(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void selectContact(Contact contact) {
        dashboardController.setSelectedContactChat(contact);
    }

    public void setSelectedContact(Contact contact) {
        this.selectedContact = contact;
    }

    public List<Contact> getContacts() {
        return dashboardController.getContacts();
    }

    public Contact getSelectedContact() {
        return selectedContact;
    }
}
