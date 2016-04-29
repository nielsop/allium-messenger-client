package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;

public interface IAuthentication {
    LoginResponseWrapper authenticate(String username, String password);
}
