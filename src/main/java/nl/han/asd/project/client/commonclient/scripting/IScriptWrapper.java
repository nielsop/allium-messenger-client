package nl.han.asd.project.client.commonclient.scripting;

/**
 * @author Marius
 * @version 1.0
 * @since 20-05-16
 */
public interface IScriptWrapper {
    boolean sendMessage(String username, String message);

    SimpleMessage[] getReceivedMessages(String date);

    void printUI(UIMessageType type, String message);

    enum UIMessageType {
        INFO, ERROR, DEBUG
    }
}
