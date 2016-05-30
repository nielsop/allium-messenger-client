package nl.han.asd.project.client.commonclient.store;

import com.google.inject.AbstractModule;

public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IContactStore.class).to(ContactStore.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(IMessageStoreObserver.class).to(MessageStore.class);
    }
}
