package nl.han.asd.project.client.commonclient.store;

import com.google.inject.AbstractModule;

import nl.han.asd.project.client.commonclient.scripting.IRunningScriptTracker;
import nl.han.asd.project.client.commonclient.scripting.RunningScriptTracker;
import nl.han.asd.project.client.commonclient.database.HyperSQLDatabase;
import nl.han.asd.project.client.commonclient.database.IDatabase;
import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.client.commonclient.persistence.PersistenceService;

import javax.inject.Singleton;

public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IScriptStore.class).to(ScriptStore.class);
        bind(IRunningScriptTracker.class).to(RunningScriptTracker.class);
        bind(IDatabase.class).to(HyperSQLDatabase.class);
        bind(IPersistence.class).to(PersistenceService.class);
        bind(IContactStore.class).to(ContactStore.class).in(Singleton.class);
        bind(IMessageStore.class).to(MessageStore.class).in(Singleton.class);
        bind(IContactManager.class).to(ContactManager.class);
    }
}
