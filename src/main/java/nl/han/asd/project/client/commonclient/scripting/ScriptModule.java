package nl.han.asd.project.client.commonclient.scripting;

import com.google.inject.AbstractModule;

public class ScriptModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IScriptWrapper.class).to(ScriptWrapper.class);
    }
}
