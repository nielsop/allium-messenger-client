package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.database.IDatabase;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
