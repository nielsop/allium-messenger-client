package nl.han.asd.project.client.commonclient.master;

import nl.han.asd.project.client.commonclient.master.wrapper.RegisterResponseWrapper;

public interface IRegistration {
    RegisterResponseWrapper register(String username, String password, String passwordRepeat);
}
