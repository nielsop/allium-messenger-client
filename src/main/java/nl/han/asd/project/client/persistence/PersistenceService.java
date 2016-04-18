package nl.han.asd.project.client.persistence;

import nl.han.asd.project.client.cryptography.IDecrypt;
import nl.han.asd.project.client.cryptography.IEncrypt;
import nl.han.asd.project.client.database.IDatabase;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
