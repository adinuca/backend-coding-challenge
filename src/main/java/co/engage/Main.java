package co.engage;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main  extends Application<ExpensesConfiguration> {

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "Expenses";
    }

    @Override
    public void initialize(Bootstrap<ExpensesConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }
    @Override
    public void run(ExpensesConfiguration configuration,
                    Environment environment) {
        environment.jersey().setUrlPattern("/app/*");
    }
}