package nl.han.asd.project.client.commonclient.database;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;

import javax.inject.Inject;

public class Database implements IDatabase {

    private IPersistence persistence;

    @Inject
    public Database(IPersistence persistence) {
        this.persistence = persistence;
    }
}
