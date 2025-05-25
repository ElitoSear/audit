package edward.audit.audit;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edward.audit.audit.Utils.*;


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

    public Ticket(String content) {
        this.id = parseId(content);
        this.date = parseDate(content);
        this.printed = parsePrinted(content, this.date);
        this.paid = parsePaid(content);
        this.payment = new Payment();

        parsePayment(
                content,
                payment,
                PaymentType.CHECK,
                "POR COBRAR\\s+\\(\\$\\s+(\\S+)\\)",
                "CHEQ/TRANS\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})");
        // Afirme
        parsePayment(
                content,
                payment,
                PaymentType.AFIRME,
                "AFIRME:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})");
        // BBVA
        parsePayment(
                content,
                payment,
                PaymentType.BBVA,
                "BANCOMER:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})");
        // RAPPI
        parsePayment(
                content,
                payment,
                PaymentType.RAPPI,
                "RAPPI:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})");
        // Uber
        parsePayment(
                content,
                payment,
                PaymentType.UBER,
                "UBER:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})");

        // Cash
        parseCash(
                content,
                payment,
                "EFECTIVO\\s+\\(\\$\\s+(\\S+)\\)",
                "TOTAL\\s+\\$\\s+(.*.\\d{2})"
        );

        System.out.println(this);
    }

    public boolean isValid() {
        return (
                this.id > 0
        );
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

    static Date parseDate(String content) {

        Date parsedDate = new Date(0L);

        Pattern pattern = Pattern.compile("Fecha:\\s+(.*)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            try {
                String day = matcher.group(1);

                Date set = dateAndTimeFormat.parse(day);

                if (day.contains("PM")) {
                    set = addHours(set, 12);
                }

                parsedDate = set;

            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error parsing date");
            }
        }

        return parsedDate;
    }

    static Date parsePrinted(String content, Date date) {

        Date parsedPrinted = new Date(0L);

        Pattern pattern = Pattern.compile("Imp:\\s+(\\S+)\\s+(\\S+)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            try {
                String time = matcher.group(1)
                        .replace(" ", "");
                String context = matcher.group(2)
                        .replace(" ", "");

                Date set = dateAndTimeFormat.parse(dateFormat.format(date) + " " + time);

                if (context.contains("PM")) {
                    set = addHours(set, 12);
                }

                parsedPrinted = set;

            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Error parsing printed date");
            }
        }

        return parsedPrinted;
    }

    static boolean parsePaid(String content) {
        return content.contains("www.grupotelnet.com.mx/facturar");
    }

    static void parsePayment(String content, Payment payment, PaymentType type, String regex, String fallbackRegex) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        double total = 0;
        double tips = 0;

        if (matcher.find()) {
            total = Double.parseDouble(matcher.group(1)
                    .replace(",", "")
                    .replace(" ", ""));
            if (matcher.groupCount() > 1) {
                tips = Double.parseDouble(matcher.group(2)
                        .replace(",", "")
                        .replace(" ", ""));
            }

            payment.set(
                    type,
                    (total + tips)
            );
        } else if (!fallbackRegex.isEmpty()) {
            Pattern secondPattern = Pattern.compile(fallbackRegex);
            Matcher secondMatcher = secondPattern.matcher(content);

            if (secondMatcher.find()) {
                total = Double.parseDouble(secondMatcher.group(1)
                        .replace(",", "")
                        .replace(" ", ""));
                if (secondMatcher.groupCount() > 1) {
                    tips = Double.parseDouble(secondMatcher.group(2)
                            .replace(",", "")
                            .replace(" ", ""));
                }

                payment.set(
                        type,
                        (total + tips)
                );
            }
        }
    }

    static void parsePayment(String content, Payment payment, PaymentType type, String regex) {
        parsePayment(content, payment, type, regex, "");
    }

    static void parseCash(String content, Payment payment, String regex, String fallbackRegex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            double total = Double.parseDouble(matcher.group(1)
                    .replace(",", "")
                    .replace(" ", ""));
            payment.set(PaymentType.CASH, total);
        } else if (payment.total() <= 0) {
            pattern = Pattern.compile(fallbackRegex);
            matcher = pattern.matcher(content);

            if (matcher.find()) {
                double total = Double.parseDouble(matcher.group(1)
                        .replace(",", "")
                        .replace(" ", ""));
                payment.set(PaymentType.CASH, total);
            }
        }
    }

    static int parseId(String content) {

        int id = 0;

        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.contains("DOMICILIO") || line.contains("LLEVAR:") || line.contains("Comedor") || line.contains("COMEDOR")
            ) {
                String substring = line.substring(line.length() - 6);

                try {
                    id = Integer.parseInt(substring.replace(" ", ""));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing ID");
                }
            }
        }
        return id;
    }

    public String format(PaymentType type) {
        return ("Folio: " + id + ", Total: $" + String.format("%.2f", payment.get(type)));
    }

    @Override
    public String toString() {
        return ("Folio: " + id + ", Total: $" + payment + ", Date: " + getDate() + ", Printed On: " + getPrinted());
    }
}