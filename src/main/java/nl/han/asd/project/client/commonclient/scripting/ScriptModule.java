package nl.han.asd.project.client.commonclient.scripting;

import com.google.inject.AbstractModule;
import nl.han.asd.project.client.commonclient.store.*;

/**
 * @author Marius
 * @version 1.0
 * @since 20-05-16
 */
public class ScriptModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IScriptWrapper.class).to(ScriptWrapper.class);
    }
}
