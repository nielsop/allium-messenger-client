package nl.han.asd.project.client.commonclient.scripting;

import com.google.inject.AbstractModule;
import nl.han.asd.project.scripting.internal.IScriptInteraction;

public class ScriptModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IScriptInteraction.class).to(ScriptInteraction.class);
    }
}
