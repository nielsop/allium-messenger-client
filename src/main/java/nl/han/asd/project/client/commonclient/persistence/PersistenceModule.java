package nl.han.asd.project.client.commonclient.persistence;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.cryptography.CryptographyService;
import nl.han.asd.project.client.commonclient.cryptography.IEncrypt;

/**
 * Created by Marius on 19-04-16.
 */
public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPersistence.class).to(PersistenceService.class);
        bind(IEncrypt.class).to(CryptographyService.class);
    }
}
