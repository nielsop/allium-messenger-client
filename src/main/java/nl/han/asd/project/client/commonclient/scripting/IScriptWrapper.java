package nl.han.asd.project.client.commonclient.scripting;

public interface IScriptWrapper {
    boolean sendMessage(String username, String message);

    SimpleMessage[] getReceivedMessages(long dateTime);

    void printUI(UIMessageType type, String message);

    enum UIMessageType {
        INFO, ERROR, DEBUG
    }
}
