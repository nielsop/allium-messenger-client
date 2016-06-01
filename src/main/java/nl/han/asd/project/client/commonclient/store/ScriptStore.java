package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.commonservices.scripting.Script;
import nl.han.asd.project.commonservices.scripting.ScriptingService;
import nl.han.asd.project.commonservices.scripting.internal.IScriptInteraction;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class ScriptStore implements IScriptStore {
    private static final String SCRIPT_NAME = "scriptName";
    private IPersistence persistence;
    private Map<String, Script> scripts = new HashMap<>();
    private IScriptInteraction scriptWrapper;

    @Inject
    public ScriptStore(IPersistence persistence, IScriptInteraction scriptWrapper) {
        this.persistence = Check.notNull(persistence, "persistence");
        this.scriptWrapper = Check.notNull(scriptWrapper, "scriptWrapper");
        setScriptsFromDatabase();
    }

    private void setScriptsFromDatabase() {
        Map<String, String> scriptsFromDatabase = persistence.getScripts();
        if(scriptsFromDatabase != null) {
            for (Map.Entry<String, String> entry : scriptsFromDatabase.entrySet()) {
                addScript(entry.getKey(), entry.getValue());
            }
        }
    }

    // TODO remove test scripts
    @Override
    public void createTestScripts() {
        addScript("Script1",
                "segment data begin \n" +
                        "text testTekst \"Dit is een test\" \n" +
                        "segment end \n" +
                        "segment main begin \n" +
                        "print testTekst \n" +
                        "segment end");
    }

    @Override
    public void addScript(String scriptName, String scriptContent) {
        Check.notNull(scriptName, SCRIPT_NAME);

        if (scriptContent != null) {
            boolean scriptAddedToDatabase = persistence.addScript(scriptName, scriptContent);
            if (scriptAddedToDatabase) {
                Script script = new ScriptingService()
                        .newScriptBuilder(scriptContent, scriptWrapper).build();
                scripts.put(scriptName, script);
            }
        }
    }

    @Override
    public void removeScript(String scriptName) {
        Check.notNull(scriptName, SCRIPT_NAME);

        boolean scriptDeletedFromDatabase = persistence.deleteScript(scriptName);
        if(scriptDeletedFromDatabase) {
            scripts.remove(scriptName);
        }
    }

    @Override
    public Script getScript(String scriptName) {
        Check.notNull(scriptName, SCRIPT_NAME);

        return scripts.get(scriptName);
    }
}
