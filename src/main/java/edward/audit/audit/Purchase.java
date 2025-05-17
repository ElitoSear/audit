package edward.audit.audit;

import java.util.Date;

public class Purchase {
    private Date date;
    private double total;
    private int id;
    private PurchaseType type;
    private String supplier;

    public Purchase(double total, int id, String supplier, Date date, PurchaseType type) {
        this.total = total;
        this.date = date;
        this.supplier = supplier;
        this.id = id;
        this.type = type;
    }

    public boolean isValid() {
        return (
                this.id > 0
        );
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PurchaseType getType() {
        return type;
    }

    public void setType(PurchaseType type) {
        this.type = type;
    }

    public String format() {
        return ( supplier + ", Total: $" + String.format("%.2f", total) + ", Folio: " + id);
    }

    @Override
    public String toString() {
        return ( supplier + ", Total: $" + String.format("%.2f", total) + ", Folio: " + id + ", Date: " + getDate());
    }
}
