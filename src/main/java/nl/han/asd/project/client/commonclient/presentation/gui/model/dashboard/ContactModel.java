package nl.han.asd.project.client.commonclient.presentation.gui.model.dashboard;

import nl.han.asd.project.client.commonclient.presentation.gui.controller.DashboardController;
import nl.han.asd.project.client.commonclient.store.Contact;

import java.util.ArrayList;

/**
 * Created by Marius on 25-04-16.
 */
public class ContactModel {
    private DashboardController dashboardController;
    private Contact currentContact;

    public ContactModel(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void selectContact(Contact contact) {
        dashboardController.selectContact(contact);
    }

    public void setCurrectContact(Contact currectContact) {
        this.currentContact = currectContact;
    }

    public ArrayList<Contact> getContacts() {
        return dashboardController.getContacts();
    }
}
