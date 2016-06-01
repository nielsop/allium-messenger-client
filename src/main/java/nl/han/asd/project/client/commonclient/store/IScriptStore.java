package nl.han.asd.project.client.commonclient.store;

import nl.han.asd.project.commonservices.scripting.Script;

import java.util.Map;

public interface IScriptStore {
    // TODO remove test method
    void createTestScripts();

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
     * Gets a script from the scriptstore by scriptname.
     *
     * @param scriptName name of the script to be fetched.
     * @return <tt>Script</tt> if found, <tt>null</tt> otherwise.
     */
    Script getScript(String scriptName);

//    /**
//     * Retrieves all scripts of the current user.
//     *
//     * @return List of Scripts
//     */
//    Map<String, Script> getAllScripts();
//
//    /**
//     * Deletes all scripts in the scriptstore memory.
//     */
//    void deleteAllScripts();
}
