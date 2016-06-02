package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.scripting.Script;
import nl.han.asd.project.scripting.ScriptingService;
import nl.han.asd.project.scripting.internal.IScriptInteraction;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.mockito.Matchers.any;

public class SendWithNoDelayMessageIT
{

    @Test public void testSendWithNoDelayMessage() throws Exception {
        String testScript = "" + "segment data begin "
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

        IScriptInteraction scriptInteraction = Mockito
                .mock(IScriptInteraction.class);

        ScriptingService.ScriptBuilder builder = new ScriptingService()
                .newScriptBuilder(testScript, scriptInteraction);
        Script script = builder.build();
        script.start();

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd H:m");
        LocalDateTime beforeExecutionTime = LocalDateTime
                .parse("2020-05-17 00:00", formatter);
        LocalDateTime onExecutionTime = LocalDateTime
                .parse("2020-05-18 22:59", formatter);
        LocalDateTime afterExecutionTime = LocalDateTime
                .parse("2030-05-19 00:00", formatter);

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

        //create messages to react to.
        IScriptInteraction.SimpleMessage returnValueDontReact = new IScriptInteraction.SimpleMessage();
        IScriptInteraction.SimpleMessage returnValueReact = new IScriptInteraction.SimpleMessage();
        returnValueDontReact.message = "Other message";
        returnValueReact.message = " delayMessage abq1 ";
        returnValueDontReact.sender = "Coen";
        returnValueReact.sender = "Coen";

        //reset script and use a new mock
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        Mockito.when(scriptInteraction.getReceivedMessages(any(Date.class)))
                .thenReturn(new IScriptInteraction.SimpleMessage[] {
                        returnValueDontReact });
        script.stopRunning();
        builder = new ScriptingService()
                .newScriptBuilder(testScript, scriptInteraction);
        script = builder.build();
        script.start();
        script.stopRunning();

        //if the date wasnt changed the message should be send
        script.update(onExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(1))
                .sendMessage(any(String.class), any(String.class));

        //reset script and use a new mock
        scriptInteraction = Mockito.mock(IScriptInteraction.class);
        Mockito.when(scriptInteraction.getReceivedMessages(any(Date.class)))
                .thenReturn(new IScriptInteraction.SimpleMessage[] {
                        returnValueReact });

        script.stopRunning();
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
