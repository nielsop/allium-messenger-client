package nl.han.onionmessenger.commonclient.persistence;

import nl.han.onionmessenger.commonclient.cryptography.IDecrypt;
import nl.han.onionmessenger.commonclient.cryptography.IEncrypt;
import nl.han.onionmessenger.commonclient.database.IDatabase;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
