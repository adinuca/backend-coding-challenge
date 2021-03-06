package co.engage.resources;


import co.engage.dao.ExpenseDAO;
import co.engage.httpclient.CurrencyProvider;
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
    private final CurrencyProvider currencyProvider;

    public ExpenseResource(ExpenseDAO dao, CurrencyProvider currencyProvider) {
        this.dao = dao;
        this.currencyProvider = currencyProvider;
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
        final double amount = getAmount(expenseJson);
        final String reason = expenseJson.get("reason").asText();
        final String date1 = expenseJson.get("date").asText();
        final java.util.Date date = sdf1.parse(date1);
        System.out.println(date.toString());
        return new Expense(amount, reason, date);
    }

    private double getAmount(JsonNode expenseJson) {
        final String amountText = expenseJson.get("amount").asText();
        if (amountText.endsWith("EUR")) {
            final String trimmedAmmont = amountText.trim();
            final String realAmount = trimmedAmmont.substring(0,trimmedAmmont.length()-4);
            return Double.parseDouble(realAmount)* currencyProvider.getExchange();
        }
        return Double.parseDouble(amountText);
    }

}
