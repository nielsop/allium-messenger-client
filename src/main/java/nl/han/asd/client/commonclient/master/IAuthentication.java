package nl.han.asd.client.commonclient.master;

import nl.han.asd.client.commonclient.master.wrapper.LoginResponseWrapper;

/**
 * Created by Kenny on 13-4-2016.
 */
public interface IAuthentication {
    LoginResponseWrapper authenticate(String username, String password);
}
