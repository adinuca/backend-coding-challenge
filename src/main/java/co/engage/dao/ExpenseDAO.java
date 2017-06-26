package co.engage.dao;

import co.engage.model.Expense;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class ExpenseDAO extends AbstractDAO<Expense> {
    public ExpenseDAO(SessionFactory factory) {
        super(factory);
    }

    public long create(Expense expense) {
        return persist(expense).getId();
    }
}