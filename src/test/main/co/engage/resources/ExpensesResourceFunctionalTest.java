package co.engage.resources;


import co.engage.dao.ExpenseDAO;
import co.engage.model.Expense;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.xml.internal.ws.server.sei.MessageFiller;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ExpensesResourceFunctionalTest {
    private static final String VALID_EXPENSE_JSON = "{\"date\":\"12/12/1988\",\"amount\":\"12.23\",\"reason\":\"aa\"}";
    private static final ExpenseDAO dao = Mockito.mock(ExpenseDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ExpenseResource(dao, null))
            .build();

    @Test
    public void shouldReturnOKWhenCreatingAnExpenseWithValidBody() {
        final Response response =  resources.target("/expenses")
                .request()
                .post(Entity.entity(VALID_EXPENSE_JSON, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void shouldReturnUnsupportedMediaTypeWhenCreatingAnExpenseWithValidBody() {
        final Response response =  resources.target("/expenses")
                .request()
                .post(Entity.entity(VALID_EXPENSE_JSON, MediaType.APPLICATION_XML_TYPE));

        assertThat(response.getStatus(), is(415));
    }

    @Test
    public void shouldReturnAllExpenses() {
        final Expense expense = new Expense(12.33d,"reason", new Date(System.currentTimeMillis()));
        final List<Expense> expenses = Collections.singletonList(expense);
        when(dao.getExpenses()).thenReturn(expenses);

        final Response response =  resources.target("/expenses")
                .request()
                .get();

        assertThat(response.getStatus(), is(200));
        assertThat(response.getMediaType(), is(MediaType.APPLICATION_JSON_TYPE));
        final GenericType<List<Expense>> genericType = new GenericType<List<Expense>>() {};
        final List<Expense> expenseList = response.readEntity(genericType);
        assertThat(expenseList.get(0), is(expense));
    }
}
