package edward.audit.audit;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edward.audit.audit.Utils.addHours;
import static edward.audit.audit.Utils.dateAndTimeFormat;

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
    public Purchase(String content) {
        this.id = parseId(content);
        this.date = parseDate(content);
        this.supplier = parseSupplier(content);
        this.total = parseTotal(content);
        this.type = parseType(content);
    }

    static double parseTotal(String content) {

        double total = 0;

        Pattern pattern = Pattern.compile(
                "TOTAL:\\s+\\$\\s+(.*.\\d{2})"
        );
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {

            total = Double.parseDouble(matcher.group(1)
                    .replace(",", "")
                    .replace(" ", ""));
        }

        return total;
    }

    static String parseSupplier(String content) {

        String supplier = "DAISUSHII";

        Pattern pattern = Pattern.compile(
                "PROVEEDOR:\\s+(.*)"
        );

        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            supplier = matcher.group(1);
        }

        return supplier;
    }

    static PurchaseType parseType(String content) {
        if (content.contains("P A G A D A")) {
            return PurchaseType.CASH;
        }

        return PurchaseType.CREDIT;
    }

    static int parseId(String content) {

        int id = 0;

        Pattern pattern = Pattern.compile(
                "COMPRA\\s+NUMERO:\\s+(\\d+)"
        );

        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {

            id = Integer.parseInt(matcher.group(1)
                    .replace(",", "")
                    .replace(" ", ""));
        }

        return id;
    }

    static Date parseDate(String content) {

        Date date = new Date(0);

        Pattern pattern = Pattern.compile("IMPRESO:\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            try {
                String day = matcher.group(1)
                        .replace(" ", "");
                String time = matcher.group(2)
                        .replace(" ", "");
                String context = matcher.group(3)
                        .replace(" ", "");

                Date set = dateAndTimeFormat.parse(day + " " + time + " " + context);

                if (context.contains("PM")) {
                    set = addHours(set, 12);
                }

                date = set;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
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
