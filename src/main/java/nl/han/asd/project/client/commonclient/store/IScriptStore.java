package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.commonservices.scripting.Script;

import java.util.List;

public interface IScriptStore {

    /**
     * Adds a new script into the scriptstore.
     *
     * @param scriptName name of the to be created script.
     * @param scriptContent the content of the to be created script.
     */
    void addScript(String scriptName, String scriptContent);

    /**
     * Removes a script from the scriptStore.
     *
     * @param scriptName name of the script that will be deleted.
     */
    void removeScript(String scriptName);

    /**
     * Gets a list of all scriptnames.
     *
     * @return <tt>List<String></tt>.
     */
    List<String> getAllScriptNames();

    /**
     * Gets the content of a script
     *
     * @param scriptName name of the script of which the content will be fetched.
     * @return <tt>String</tt> containing the content of a script
     */
    String getScriptContent(String scriptName);

    /**
     * Updates the content of a script
     *
     * @param scriptName name of the script to be updated.
     * @param scriptContent the content of the script that will be replaced.
     */
    void updateScript(String scriptName, String scriptContent);
}
