package nl.han.asd.project.client.commonclient.login;

import com.google.inject.AbstractModule;

public class LoginModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ILoginService.class).to(LoginService.class);
    }
}
