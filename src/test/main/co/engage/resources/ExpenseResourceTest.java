package co.engage.resources;

import co.engage.dao.ExpenseDAO;
import co.engage.model.Expense;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExpenseResourceTest {

    private static final String VALID_EXPENSE_JSON = "{\"date\":\"12/12/1988\",\"amount\":\"12.23\",\"reason\":\"aa\"}";
    private static final String EXPENSE_WITH_INVALID_DATE_JSON = "{\"date\":\"12/1988\",\"amount\":\"12.23\",\"reason\":\"aa\"}";
    private static final String EXPENSE_WITH_INVALID_AMOUNT = "{\"date\":\"12/12/1988\",\"amount\":\"12d.23\",\"reason\":\"aa\"}";
    private static final String INVALID_EXPENSE_JSON = "{pksda";
    @Mock
    private ExpenseDAO expenseDAO;

    private ExpenseResource unit;

    @Before
    public void setUp() throws Exception {
        unit = new ExpenseResource(expenseDAO);
    }

    @Test
    public void shouldReturnBadRequestWhenJsonIsInvalid() throws Exception {
        final Response response = unit.saveExpense(INVALID_EXPENSE_JSON);

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void shouldReturnBadRequestWhenJsonIsNull() throws Exception {
        final Response response = unit.saveExpense(null);

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void shouldReturnBadRequestWhenJsonIsEmpty() throws Exception {
        final Response response = unit.saveExpense("");

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void shouldReturnBadRequestWhenDateIsInvalid() throws Exception {
        final Response response = unit.saveExpense(EXPENSE_WITH_INVALID_DATE_JSON);

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void shouldReturnBadRequestWhenAmountIsInvalid() throws Exception {
        final Response response = unit.saveExpense(EXPENSE_WITH_INVALID_AMOUNT);

        assertThat(response.getStatus(), is(400));
    }

    @Test
    public void shouldReturnOKWhenSavingValidJson() throws Exception {
        final Response response = unit.saveExpense(VALID_EXPENSE_JSON);

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnServerErrorWhenSavingThrowsException() throws Exception {
        when(expenseDAO.create(any(Expense.class))).thenThrow(SQLException.class);

        final Response response = unit.saveExpense(VALID_EXPENSE_JSON);

        assertThat(response.getStatus(), is(500));
    }

    @Test
    public void shouldReturnServerErrorWhenGettingExpenseshrowsException() throws Exception {
        when(expenseDAO.getExpenses()).thenThrow(SQLException.class);

        final Response response = unit.getAllExpenses();

        assertThat(response.getStatus(), is(500));
    }

    @Test
    public void shouldReturnAllExpensesFromDB() throws Exception {
        final Expense expense = new Expense(12.33d,"reason", new Date(System.currentTimeMillis()));
        final List<Expense> expenses = Collections.singletonList(expense);
        when(expenseDAO.getExpenses()).thenReturn(expenses);

        final Response response = unit.getAllExpenses();

        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is(expenses));
    }
}