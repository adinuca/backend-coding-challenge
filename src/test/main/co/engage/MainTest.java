package co.engage;

import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

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
    private ArgumentCaptor<Bundle> bundleArgumentCaptoraptor;

    private Main unit;

    @Before
    public void setUp() throws Exception {
        when(environment.jersey()).thenReturn(jerseyEnvironment);

        unit = new Main();
    }

    @Test
    public void shouldRegisterAnAssetsBundle() throws Exception {
        unit.initialize(bootstrap);

        verify(bootstrap, times(3)).addBundle(bundleArgumentCaptoraptor.capture());
        final List<Bundle> allValues = bundleArgumentCaptoraptor.getAllValues();
        final Optional<Bundle> assetsBundle = getBundleRegisteredOfType(allValues, AssetsBundle.class);
        assertThat(assetsBundle.isPresent(), is(true));
        final AssetsBundle bundle = (AssetsBundle) assetsBundle.get();
        assertThat(bundle.getIndexFile(), is("index.html"));
        assertThat(bundle.getResourcePath(), is("/assets/"));
        assertThat(bundle.getUriPath(), is("/"));
    }

    @Test
    @Ignore
    // @todo
    public void shouldRegisterMigrationBundle() throws Exception {
        unit.initialize(bootstrap);

        verify(bootstrap, times(3)).addBundle(bundleArgumentCaptoraptor.capture());
        final List<Bundle> allValues = bundleArgumentCaptoraptor.getAllValues();
        final Optional<Bundle> migrationBundleRegistered = getBundleRegisteredOfType(allValues, MigrationsBundle.class);
        assertThat(migrationBundleRegistered.isPresent(), is(true));
    }

    @Test
    @Ignore
    // @todo
    public void shouldRegisterHibernateBundle() throws Exception {
        unit.initialize(bootstrap);

        verify(bootstrap, times(3)).addBundle(bundleArgumentCaptoraptor.capture());
        final List<Bundle> allValues = bundleArgumentCaptoraptor.getAllValues();
        final Optional<Bundle> migrationBundleRegistered = getBundleRegisteredOfType(allValues, HibernateBundle.class);
        assertThat(migrationBundleRegistered.isPresent(), is(true));
    }

    @Test
    public void shouldServeResourcesEndpointFromAppPath() throws Exception {
        unit.run(configuration, environment);

        verify(jerseyEnvironment).setUrlPattern("/app/*");
    }

    private Optional<Bundle> getBundleRegisteredOfType(List<Bundle> allValues, Class c) {
     return allValues.stream()
             .filter(value -> value.getClass().equals(c))
             .findFirst();
    }
}