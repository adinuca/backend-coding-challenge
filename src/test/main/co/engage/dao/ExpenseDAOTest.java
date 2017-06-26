package co.engage.dao;

import co.engage.model.Expense;
import io.dropwizard.testing.junit.DAOTestRule;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.Date;
import java.util.List;

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

    @Test
    public void shouldRetrieveAllExpenses(){
        final Expense expense1 = new Expense(12.2, "abc", new Date(System.currentTimeMillis()));
        final Expense expense2 = new Expense(12.5, "ab11c", new Date(System.currentTimeMillis()-1000));
        final Expense expense3 = new Expense(12.1, "a111bc", new Date(System.currentTimeMillis()+23));

        database.inTransaction(() -> {
            expenseDAO.create(expense1);
            expenseDAO.create(expense2);
            expenseDAO.create(expense3);
        });

        final List<Expense> expenses = database.inTransaction(() -> expenseDAO.getExpenses());
        assertThat(expenses.size(), is(3));
    }

    @Test
    public void shouldRetrieveExistingExpense(){
        final Expense expense1 = new Expense(12.2, "abc", new Date(System.currentTimeMillis()));

        database.inTransaction(() -> {
            expenseDAO.create(expense1);
        });

        final List<Expense> expenses = database.inTransaction(() -> expenseDAO.getExpenses());
        assertThat(expenses.size(), is(1));
        assertThat(expenses.get(0).getReason(), is(expense1.getReason()));
        assertThat(expenses.get(0).getAmount(), is(expense1.getAmount()));
        assertThat(expenses.get(0).getDate(), is(expense1.getDate()));
    }
}