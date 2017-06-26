package co.engage.model;


import javax.persistence.*;
import java.sql.Date;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    @Column
    private double amount;
    @Column
    private String reason;
    @Column
    private Date date;

    private Expense() {
    }

    public Expense(double amount, String reason, Date date) {
        this.amount = amount;
        this.reason = reason;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (id != expense.id) return false;
        if (Double.compare(expense.amount, amount) != 0) return false;
        if (reason != null ? !reason.equals(expense.reason) : expense.reason != null) return false;
        return date != null ? date.toString().equals(expense.getDate()
                .toString()) : expense.date == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
