package co.engage;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {
    @Mock
    private Bootstrap<ExpensesConfiguration> bootstrap;
    @Mock
    private Environment environment;
    @Mock
    private ExpensesConfiguration configuration;
    @Mock
    private JerseyEnvironment jerseyEnvironment;
    @Captor
    private ArgumentCaptor<AssetsBundle> captor;

    private Main unit;

    @Before
    public void setUp() throws Exception {
        when(environment.jersey()).thenReturn(jerseyEnvironment);

        unit = new Main();
    }

    @Test
    public void shouldRegisterAnAssetsBundle() throws Exception {
        unit.initialize(bootstrap);

        verify(bootstrap).addBundle(captor.capture());
        final AssetsBundle assetsBundle = captor.getValue();
        assertThat(assetsBundle.getIndexFile(), is("index.html"));
        assertThat(assetsBundle.getResourcePath(), is("/assets/"));
        assertThat(assetsBundle.getUriPath(), is("/"));
    }

    @Test
    public void shouldServeResourcesEndpointFromAppPath() throws Exception {
        unit.run(configuration, environment);

        verify(jerseyEnvironment).setUrlPattern("/app/*");
    }
}