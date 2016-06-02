package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;

import javax.inject.Inject;
import java.util.List;

public class ScriptStore implements IScriptStore {
    private static final String SCRIPT_NAME = "scriptName";
    private IPersistence persistence;

    @Inject
    public ScriptStore(IPersistence persistence) {
        this.persistence = Check.notNull(persistence, "persistence");
    }

    @Override
    public void addScript(String scriptName, String scriptContent) {
        Check.notNull(scriptName, SCRIPT_NAME);

        if (scriptContent != null) {
            persistence.addScript(scriptName, scriptContent);
        }
    }

    @Override
    public void removeScript(String scriptName) {
        Check.notNull(scriptName, SCRIPT_NAME);

        persistence.deleteScript(scriptName);
    }

    @Override
    public List<String> getAllScriptNames() {
        return persistence.getAllScriptNames();
    }

    @Override
    public String getScriptContent(String scriptName) {
        return persistence.getScriptContent(scriptName);
    }

    @Override
    public void updateScript(String scriptName, String scriptContent) {
       persistence.updateScript(scriptName, scriptContent);
    }
}
