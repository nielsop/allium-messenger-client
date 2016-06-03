package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.scripting.internal.IScriptInteraction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class RunningScriptTrackerTest
{
    IScriptInteraction scriptInteraction;
    RunningScriptTracker sut;

    @Before
    public void setup()
    {
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        sut = new RunningScriptTracker(scriptInteraction);
    }

    @Test
    public void startScriptInvalidScriptTest()
    {
        assertFalse(sut.startScript("name", "not a script"));

    }
    @Test
    public void startScriptSuccess()
    {
        assertTrue(sut.startScript("name", "segment main begin print \"test\" segment end"));
    }

    @Test
    public void stopScriptNoExceptionOnNonexistendScript()
    {
        sut.stopScript("not a script");
    }

    @Test
    public void stopScriptTest()
    {
        sut.startScript("name", "segment main begin print \"test\" segment end");
        sut.stopScript("name");
    }

    @Test
    public void StopScriptNoExceptionOnNull()
    {
        sut.stopScript(null);
    }


}
