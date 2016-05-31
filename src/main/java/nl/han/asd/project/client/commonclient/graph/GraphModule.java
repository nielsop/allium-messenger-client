package nl.han.asd.project.client.commonclient.graph;

import com.google.inject.AbstractModule;

public class GraphModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IGetVertices.class).to(GraphManagerService.class);
    }
}
