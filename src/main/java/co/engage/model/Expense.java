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
}
