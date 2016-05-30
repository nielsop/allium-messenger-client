package nl.han.asd.project.client.commonclient.persistence;

import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IPersistence.class).to(PersistenceService.class);
    }
}
