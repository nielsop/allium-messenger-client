package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.commonservices.internal.utility.Check;
import nl.han.asd.project.commonservices.scripting.Script;
import nl.han.asd.project.commonservices.scripting.ScriptingService;
import nl.han.asd.project.commonservices.scripting.internal.IScriptInteraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * {@inheritDoc}
 */
public class RunningScriptTracker implements IRunningScriptTracker {

    private Map<String, Script> scripts = new HashMap<>();
    private IScriptInteraction scriptInteraction;
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningScriptTracker.class);

    @Inject
    public RunningScriptTracker(IScriptInteraction scriptInteraction) {
        Check.notNull(scriptInteraction, "scriptInteraction");
        this.scriptInteraction = scriptInteraction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean startScript(String scriptName, String scriptContent) {

        Script script = new ScriptingService()
                .newScriptBuilder(scriptContent, scriptInteraction).build();
        if(script == null){
            LOGGER.debug("Invalid script entered: ");
            return false;
        }
        scripts.put(scriptName, script);
        script.start();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopScript(String scriptName) {
        Script scriptToStop = scripts.get(scriptName);
        if (scriptToStop != null) {
            scriptToStop.stop();
            scripts.remove(scriptName);
        }
    }
}
