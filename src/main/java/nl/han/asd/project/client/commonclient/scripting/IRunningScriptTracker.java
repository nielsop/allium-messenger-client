package nl.han.asd.project.client.commonclient.scripting;

public interface IRunningScriptTracker {

    /**
     * Makes a script start running
     *
     * @param scriptName name of the to script to be started.
     * @param scriptContent content of the to script to be started.
     */
    boolean startScript(String scriptName, String scriptContent);

    /**
    * Makes a script stop running
    *
    * @param scriptName name of the to script to be stopped.
    */
    void stopScript(String scriptName);
}
