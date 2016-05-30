package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;

/**
 * Created by Marius on 19-04-16.
 */
public interface ILoginService {
    LoginResponseWrapper login(String username, String password);

    boolean logout(String username, String secretHash);
}
