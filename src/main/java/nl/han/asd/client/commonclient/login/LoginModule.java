package nl.han.asd.client.commonclient.login;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class LoginModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ILogin.class).to(LoginService.class);
    }
}
