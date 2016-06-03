package nl.han.asd.project.client.commonclient.store;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IContactStore.class).to(ContactStore.class).in(Singleton.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(IContactManager.class).to(ContactManager.class);
    }
}
