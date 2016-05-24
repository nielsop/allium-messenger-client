package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;

import javax.inject.Inject;

public class PersistenceService implements IPersistence {
    private IDatabase database;

    @Inject
    public PersistenceService(IDatabase database) {
        this.database = database;
    }
}
