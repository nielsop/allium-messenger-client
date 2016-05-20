package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class ContactStore implements IContact {
    private IPersistence persistence;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }
}
