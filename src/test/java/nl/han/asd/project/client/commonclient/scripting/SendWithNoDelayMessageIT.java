package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.scripting.Script;
import nl.han.asd.project.scripting.ScriptingService;
import nl.han.asd.project.scripting.ScriptingService.ScriptBuilder;
import nl.han.asd.project.scripting.internal.IScriptInteraction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.mockito.Matchers.any;

public class SendWithNoDelayMessageIT
{

    String testScript;
    IScriptInteraction scriptInteraction;
    Script script;
    ScriptBuilder builder;
    DateTimeFormatter formatter;
    LocalDateTime beforeExecutionTime;
    LocalDateTime onExecutionTime;
    LocalDateTime afterExecutionTime;

    @Before
    public void setup()
    {
        testScript = "" + "segment data begin "
                + "     text testMessage value \"If you receive this message, I did not delay it and I am in trouble. Send help! \" "
                + "     text reactionMessage value \"delayMessage abq1\" "
                + "     datetime testMessageSendTime value 2020-05-18 "
                + "segment end " + "segment contact begin "
                + "     person mark value \"MarkVaessen\" " + "segment end "
                + "segment main begin "
                + "     schedule testMessageSendTime begin "
                + "         send using testMessage as message mark as contact "
                + "     end " + "     react reactionMessage begin "
                + "         set testMessageSendTime value 2040-05-18 "
                + "     end " + "segment end ";

        formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd H:m");
        beforeExecutionTime = LocalDateTime
                .parse("2020-05-17 00:00", formatter);
        onExecutionTime = LocalDateTime
                .parse("2020-05-18 22:59", formatter);
        afterExecutionTime = LocalDateTime
                .parse("2030-05-19 00:00", formatter);

    }

    @Test
    public void messageOnATimerTest()
    {
        scriptInteraction = Mockito
                .mock(IScriptInteraction.class);

        builder = new ScriptingService()
                .newScriptBuilder(testScript, scriptInteraction);
        script = builder.build();
        script.start();

        // dont send message if execution time is not reached
        script.update(beforeExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(0))
                .sendMessage(any(String.class), any(String.class));

        //  send message if execution time is reached
        script.update(onExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(1))
                .sendMessage(any(String.class), any(String.class));

        // dont send message twice
        script.update(onExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(1))
                .sendMessage(any(String.class), any(String.class));

        script.stopRunning();
    }


    @Test
    public void notChangedDateShouldBeSendTest() throws Exception {
        //create messages to react to.
        IScriptInteraction.SimpleMessage returnValueDontReact = new IScriptInteraction.SimpleMessage();
        returnValueDontReact.message = "Other message";
        returnValueDontReact.sender = "Coen";

        //setup mock
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        Mockito.when(scriptInteraction.getReceivedMessages(any(Date.class)))
                .thenReturn(new IScriptInteraction.SimpleMessage[] {
                        returnValueDontReact });

        builder = new ScriptingService()
                .newScriptBuilder(testScript, scriptInteraction);
        script = builder.build();
        script.start();
        script.stopRunning();

        //if the date wasnt changed the message should be send
        script.update(onExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(1))
                .sendMessage(any(String.class), any(String.class));

        script.stopRunning();
    }

    @Test
    public void changedMessageTimeShouldntSendTest()
    {
        IScriptInteraction.SimpleMessage returnValueReact = new IScriptInteraction.SimpleMessage();
        returnValueReact.message = " delayMessage abq1 ";
        returnValueReact.sender = "Coen";

        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        Mockito.when(scriptInteraction.getReceivedMessages(any(Date.class)))
                .thenReturn(new IScriptInteraction.SimpleMessage[] {
                        returnValueReact });

        builder = new ScriptingService()
                .newScriptBuilder(testScript, scriptInteraction);
        script = builder.build();
        script.start();
        script.stopRunning();

        // if the date is change the message shouldnt be send.
        script.update(onExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(0))
                .sendMessage(any(String.class), any(String.class));
    }

}
