package nl.han.asd.project.client.commonclient.scripting;

import nl.han.asd.project.scripting.Script;
import nl.han.asd.project.scripting.ScriptingService;
import nl.han.asd.project.scripting.internal.IScriptInteraction;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Matchers.any;

public class TimedMessageIT
{

    @Test
    public void timedMessage() throws Exception
    {
        String testScript = ""
                + "segment data begin "
                + "     text testMessage value \"This message has been send after I left te country. \" "
                + "     datetime testMessageSendTime value 2020-05-18 11:00 "
                + "segment end "
                + "segment contact begin "
                + "     person mark value \"MarkVaessen\" "
                + "segment end "
                + "segment main begin "
                + "     schedule testMessageSendTime begin "
                + "         send using testMessage as message mark as contact "
                + "     end "
                + "segment end ";

        IScriptInteraction scriptInteraction = Mockito.mock(IScriptInteraction.class);

        ScriptingService.ScriptBuilder builder = new ScriptingService().newScriptBuilder(testScript, scriptInteraction);
        Script script = builder.build();
        script.start();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime beforeExecutionTime = LocalDateTime.parse("2016-05-18 11:30", formatter);
        LocalDateTime afterExecutionTime = LocalDateTime.parse("2020-05-18 11:30", formatter);

        script.update(beforeExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(0)).sendMessage(any(String.class),any(String.class));

        script.update(afterExecutionTime);
        Mockito.verify(scriptInteraction, Mockito.times(1)).sendMessage(any(String.class),any(String.class));

    }




}
