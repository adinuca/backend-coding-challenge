package co.engage.resources;


import co.engage.dao.ExpenseDAO;
import co.engage.model.Expense;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.serverError;
import static javax.ws.rs.core.Response.status;

@Path("expenses")
public class ExpenseResource {
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
    private static final Logger logger = LoggerFactory.getLogger(ExpenseResource.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ExpenseDAO dao;

    public ExpenseResource(ExpenseDAO dao) {
        this.dao = dao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response saveExpense(String request) {
        try {
            final Expense expense = createExpenseFromJson(request);
            dao.create(expense);
            return ok().build();
        }catch(IllegalArgumentException|IOException|ParseException e){
            logger.warn("Json for expense creation is invalid {}", request, e);
            return status(Response.Status.BAD_REQUEST).build();

        }catch (Exception e){
            logger.error("Exception occured while saving expense {}", request, e);
            e.printStackTrace();
            return serverError().build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork(readOnly=true)
    public Response getAllExpenses() {
        try{
            return ok(dao.getExpenses()).build();
        }catch (Exception e){
            e.printStackTrace();
            return serverError().build();
        }
    }

    private Expense createExpenseFromJson(String request) throws IOException, ParseException {
        if (request == null){
            throw new IllegalArgumentException("String is null");
        }
        final JsonNode expenseJson = OBJECT_MAPPER.readTree(request);
        expenseJson.get("date");
        final double amount = Double.parseDouble(expenseJson.get("amount").asText());
        final String reason = expenseJson.get("reason").asText();
        final String date1 = expenseJson.get("date").asText();
        final java.util.Date parse = sdf1.parse(date1);
        final Date date =  new Date(parse.getTime());
        return new Expense(amount, reason, date);
    }

}
