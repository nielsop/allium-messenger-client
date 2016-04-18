package nl.han.asd.project.client.commonclient.message;

/**
 * Created by Julius on 18/04/16.
 */
public class Message {
    private String ID;
    private String text;
    private String sender;

    public Message(String text, String sender){
        this.text = text;
        this.sender = sender;
    }
}
