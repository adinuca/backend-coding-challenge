package co.engage.resources;


import co.engage.dao.ExpenseDAO;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ExpensesResourceFunctionalTest {
    private static final String VALID_EXPENSE_JSON = "{\"date\":\"12/12/1988\",\"amount\":\"12.23\",\"reason\":\"aa\"}";
    private static final ExpenseDAO dao = Mockito.mock(ExpenseDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ExpenseResource(dao))
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
}
