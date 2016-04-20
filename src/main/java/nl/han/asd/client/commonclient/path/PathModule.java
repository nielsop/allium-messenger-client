package nl.han.asd.client.commonclient.path;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class PathModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetPath.class).to(PathDeterminationService.class);
    }
}
