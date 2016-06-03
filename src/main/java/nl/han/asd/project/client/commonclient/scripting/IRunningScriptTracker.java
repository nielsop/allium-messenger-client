package nl.han.asd.project.client.commonclient.scripting;

/**
 * Keeps track of which scripts are running.
 * Provides functionality to start a existing script
 * Provides functionality to stop a running script.
 */
public interface IRunningScriptTracker {

    /**
     * Starts a script. A script runs on it own Thread.
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
