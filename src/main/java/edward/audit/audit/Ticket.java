package edward.audit.audit;

import java.util.Date;


public class Ticket {
    private Date date;
    private Date printed;
    private int id;
    private boolean paid;
    private double total;
    private TicketType type;

    public Ticket(boolean paid, double total, int id, Date date, Date printed) {
        this(paid, total, id, date, printed, TicketType.CASH);
    }

    public Ticket(boolean paid, double total, int id, Date date, Date printed, TicketType type) {
        this.paid = paid;
        this.total = total;
        this.id = id;
        this.date = date;
        this.printed = printed;
        this.type = type;
    }

    public boolean isValid() {
        return (
                this.id > 0
                //&& this.total > 0
        );
    }

    public boolean update(Ticket ticket) {
        if (ticket.getPrinted().getTime() > this.printed.getTime()) {
            this.paid = ticket.isPaid();
            this.total = ticket.getTotal();
            this.id = ticket.getId();
            this.date = ticket.getDate();
            this.printed = ticket.getPrinted();
            this.type = ticket.getType();
            return true;
        }
        return false;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        //return String.format("Folio: %d, Total: $%.2f", id, total);
        return ("Folio: " + id + ", Total: " + total + ", Date: " + getDate() + ", Printed On: " + getPrinted());
    }
}