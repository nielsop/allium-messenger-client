package nl.han.asd.project.client.commonclient.path;

import com.google.inject.AbstractModule;

/**
 * Created by Marius on 19-04-16.
 */
public class PathModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetMessagePath.class).to(PathDeterminationService.class);
    }
}
