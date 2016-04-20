package nl.han.asd.client.commonclient.store;

import nl.han.asd.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class ContactStore implements IContact {
    public IPersistence persistence;

    @Inject
    public ContactStore(IPersistence persistence) {
        this.persistence = persistence;
    }
}
