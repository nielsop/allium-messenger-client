package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;

/**
 * Created by Marius on 19-04-16.
 */
@FunctionalInterface
public interface ILogin {
    LoginResponseWrapper login(String username, String password);
}
