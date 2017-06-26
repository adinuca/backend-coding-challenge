package co.engage;


import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AssetsBundleFunctionalTest {
    @Rule
    public final DropwizardAppRule<ExpensesConfiguration> RULE =
            new DropwizardAppRule<ExpensesConfiguration>(Main.class,
                    ResourceHelpers.resourceFilePath("test-config.yaml"));

    @Test
    public void shouldReturnFileFromAssets() {
        final Client client = new JerseyClientBuilder().build();
        final Response response =  client.target(
                String.format("http://localhost:%d/index.html", RULE.getLocalPort()))
                .request().get();

        assertThat(response.getStatus(), is(200));
        assertThat(response.readEntity(String.class), is("Test index html"));
    }
}
