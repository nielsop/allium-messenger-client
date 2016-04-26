package nl.han.asd.project.client.commonclient.persistence;


import nl.han.asd.project.client.commonclient.cryptography.IDecrypt;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;
import nl.han.asd.project.client.commonclient.database.IDatabase;

import javax.inject.Inject;

public class PersistenceService implements IPersistence {
    public IDatabase database;
    public IEncrypt encrypt;
    public IDecrypt decrypt;

    @Inject
    public PersistenceService(IDatabase database, IEncrypt encrypt, IDecrypt decrypt) {
        this.database = database;
        this.encrypt = encrypt;
        this.decrypt = decrypt;
    }
}
