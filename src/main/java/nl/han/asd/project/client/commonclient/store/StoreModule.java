package nl.han.asd.project.client.commonclient.store;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.scripting.IRunningScriptTracker;
import nl.han.asd.project.client.commonclient.scripting.RunningScriptTracker;

public class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IContactStore.class).to(ContactStore.class);
        bind(IMessageStore.class).to(MessageStore.class);
        bind(IScriptStore.class).to(ScriptStore.class);
        bind(IScriptStore.class).to(ScriptStore.class);
        bind(IScriptStore.class).to(ScriptStore.class);
        bind(IRunningScriptTracker.class).to(RunningScriptTracker.class);
    }
}
