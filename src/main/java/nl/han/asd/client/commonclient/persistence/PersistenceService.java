package nl.han.asd.client.commonclient.persistence;

import nl.han.asd.client.commonclient.database.IDatabase;

import javax.inject.Inject;

public class PersistenceService implements IPersistence {
    public IDatabase database;

    @Inject
    public PersistenceService(IDatabase database) {
        this.database = database;
    }
}
