package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;

/**
 * Created by Kenny on 13-4-2016.
 */
@FunctionalInterface
public interface IAuthentication {
    LoginResponseWrapper authenticate(String username, String password);
}
