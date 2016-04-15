package unit.commonclient.persistence;

import unit.commonclient.cryptography.IDecrypt;
import unit.commonclient.cryptography.IEncrypt;
import unit.commonclient.database.IDatabase;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;
}
