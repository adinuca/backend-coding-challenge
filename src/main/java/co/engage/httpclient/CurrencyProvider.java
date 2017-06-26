package co.engage.httpclient;


import com.fasterxml.jackson.databind.JsonNode;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyWebTarget;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.Map;

public class CurrencyProvider {
    private JerseyClient jerseyClient;
    private String url;

    public CurrencyProvider(JerseyClient jerseyClient, String url){
        this.jerseyClient = jerseyClient;
        this.url = url;
    }


    public double getExchange() {
        final JerseyWebTarget target = jerseyClient.target(url);
        final Response response = target.request().buildGet().invoke();
        //todo validation on the status of the response
        final JsonNode jsonNode = response.readEntity(JsonNode.class);
        return jsonNode.get("rates").get("GBP").asDouble();
    }
}
