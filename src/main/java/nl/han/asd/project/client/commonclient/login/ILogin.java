package nl.han.asd.project.client.commonclient.login;

import nl.han.asd.project.client.commonclient.master.wrapper.LoginResponseWrapper;

public interface ILogin {
    LoginResponseWrapper login(String username, String password);
}
