package co.engage;

import co.engage.dao.ExpenseDAO;
import co.engage.model.Expense;
import co.engage.resources.ExpenseResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Main  extends Application<ExpensesConfiguration> {

    private HibernateBundle<ExpensesConfiguration> hibernate;

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

    @Override
    public String getName() {
        return "Expenses";
    }

    @Override
    public void initialize(Bootstrap<ExpensesConfiguration> bootstrap) {
        hibernate = new HibernateBundle<ExpensesConfiguration>(Expense.class) {
            public DataSourceFactory getDataSourceFactory(ExpensesConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

        final MigrationsBundle<ExpensesConfiguration> migrationsBundle = new MigrationsBundle<ExpensesConfiguration>() {
            public DataSourceFactory getDataSourceFactory(ExpensesConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(migrationsBundle);
    }
    @Override
    public void run(ExpensesConfiguration configuration,
                    Environment environment) {
        final ExpenseDAO dao = new ExpenseDAO(hibernate.getSessionFactory());
        environment.jersey().setUrlPattern("/app/*");
        environment.jersey().register(new ExpenseResource(dao));
    }
}