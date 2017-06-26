package co.engage;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExpensesConfiguration  extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private String exchangeRateApiUrl;

    public String getExchangeRateApiUrl() {
        return exchangeRateApiUrl;
    }

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
