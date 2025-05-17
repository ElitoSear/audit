package edward.audit.audit;

import java.util.Date;


public class Ticket {
    private Date date;
    private Date printed;
    private int id;
    private boolean paid;
    private Payment payment;
    private int calpishitos = 0;

    public Ticket(boolean paid, Payment payment, int id, Date date, Date printed) {
        this.paid = paid;
        this.payment = payment;
        this.id = id;
        this.date = date;
        this.printed = printed;
    }

    public boolean isValid() {
        return (
                this.id > 0
        );
    }

    public boolean update(Ticket ticket) {
        if (ticket.getPrinted().getTime() > this.printed.getTime()) {
            this.paid = ticket.isPaid();
            this.payment = ticket.getPayment();
            this.id = ticket.getId();
            this.date = ticket.getDate();
            this.printed = ticket.getPrinted();
            this.payment = ticket.getPayment();
            return true;
        }
        return false;
    }

    public int getCalpishitos() {
        return calpishitos;
    }

    public void setCalpishitos(int calpishitos) {
        this.calpishitos = calpishitos;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getPrinted() {
        return printed;
    }

    public void setPrinted(Date printed) {
        this.printed = printed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Payment getPayment() {
        return payment;
    }

    double getTotal() {
        return payment.total();
    }

    public String format(PaymentType type) {
        return ("Folio: " + id + ", Total: $" + String.format("%.2f" , payment.get(type)));
    }

    @Override
    public String toString() {
        return ("Folio: " + id + ", Total: $" + payment + ", Date: " + getDate() + ", Printed On: " + getPrinted());
    }
}