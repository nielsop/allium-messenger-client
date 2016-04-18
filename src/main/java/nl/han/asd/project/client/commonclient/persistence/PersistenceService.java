package nl.han.asd.project.client.commonclient.persistence;

import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
