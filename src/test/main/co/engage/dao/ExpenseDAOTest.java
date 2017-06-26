package co.engage.dao;

import co.engage.model.Expense;
import io.dropwizard.testing.junit.DAOTestRule;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

public class ExpenseDAOTest {
    @Rule
    public DAOTestRule database = DAOTestRule.newBuilder().addEntityClass(Expense.class).build();

    private ExpenseDAO expenseDAO;

    @Before
    public void setUp() {
        expenseDAO = new ExpenseDAO(database.getSessionFactory());
    }

    @Test
    public void shouldCreatesExpense() {
        final double amount = 12.2d;
        final String reason = "test reason";
        final Date date = new Date(System.currentTimeMillis());
        final Expense expense = new Expense(amount, reason, date);

        long id = database.inTransaction(() -> expenseDAO.create(expense));

        final Expense expenseInDb = database.inTransaction(()->{
            final Session currentSession = database.getSessionFactory().getCurrentSession();
            return currentSession.get(Expense.class, id);
        });
        assertThat(expense.getId(), notNullValue());
        assertThat(expenseInDb.getReason(), is(reason));
        assertThat(expenseInDb.getAmount(), is(amount));
        assertThat(expenseInDb.getDate(), is(date));
    }
}