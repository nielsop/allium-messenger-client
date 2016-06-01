package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.client.commonclient.persistence.IPersistence;
import nl.han.asd.project.commonservices.scripting.internal.IScriptInteraction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;

public class ScriptStoreTest {

    private IPersistence persistence;
    private ScriptStore scriptStore;
    private IScriptInteraction scriptInteraction;

    @Before
    public void initialize() {
        persistence = Mockito.mock(IPersistence.class);
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        Mockito.when(persistence.getScripts()).thenReturn(null);
        scriptStore = new ScriptStore(persistence, scriptInteraction);
    }

    @Test
    public void addSingleScript() {
        String scriptName = "script1";
        String scriptContent = "segment main begin print \"Hoi\" segment end";

        Mockito.when(persistence.addScript(any(String.class), any(String.class))).thenReturn(true);

        scriptStore.addScript(scriptName, scriptContent);

        Assert.assertNotNull(scriptStore.getScript(scriptName));
    }

    @Test
    public void removeScript() {
        String scriptName = "script1";
        String scriptContent = "segment main begin print \"Hoi\" segment end";

        Mockito.when(persistence.addScript(any(String.class), any(String.class))).thenReturn(true);
        scriptStore.addScript(scriptName, scriptContent);

        Mockito.when(persistence.deleteScript(any(String.class))).thenReturn(true);
        scriptStore.removeScript(scriptName);

        Assert.assertNull(scriptStore.getScript(scriptName));
    }
}
