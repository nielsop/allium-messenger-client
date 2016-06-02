package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.commonservices.scripting.internal.IScriptInteraction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class RunningScriptTrackerTest
{
    IScriptInteraction scriptInteraction;

    @Before
    public void setup()
    {
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
    }

    @Test
    public void startScriptInvalidScriptTest()
    {
        RunningScriptTracker sut = new RunningScriptTracker(scriptInteraction);
        assertFalse(sut.startScript("name", "not a script"));

    }
    @Test
    public void startScriptSuccess()
    {
        RunningScriptTracker sut = new RunningScriptTracker(scriptInteraction);
        assertTrue(sut.startScript("name", "segment main begin print \"test\" segment end"));
    }
    @Test
    public void stopScriptTest()
    {

    }
    @Test
    public void awdwad3()
    {

    }

}
