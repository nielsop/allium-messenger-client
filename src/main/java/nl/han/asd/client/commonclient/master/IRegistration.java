package nl.han.asd.client.commonclient.master;

import nl.han.asd.client.commonclient.master.wrapper.RegisterResponseWrapper;

/**
 * Created by Kenny on 13-4-2016.
 */
public interface IRegistration {

    RegisterResponseWrapper register(String username, String password);
}
