package co.engage.dao;

import co.engage.model.Expense;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ExpenseDAO extends AbstractDAO<Expense> {
    public ExpenseDAO(SessionFactory factory) {
        super(factory);
    }

    public long create(Expense expense) {
        return persist(expense).getId();
    }

    public List<Expense> getExpenses(){
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Expense> criteria = builder.createQuery(Expense.class);
        final Root<Expense> root = criteria.from( Expense.class );
        criteria.select( root );
        return  list(criteria);
    }
}