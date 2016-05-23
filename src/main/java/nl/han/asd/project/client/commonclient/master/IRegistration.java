package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;

/**
 * Created by Kenny on 13-4-2016.
 */
@FunctionalInterface
public interface IRegistration {

    RegisterResponseWrapper register(String username, String password);
}
