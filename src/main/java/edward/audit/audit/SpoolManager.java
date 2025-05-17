package edward.audit.audit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SpoolManager {
    public static final Path SPOOLS_DIRECTORY = Path.of("C:/Windows/System32/spool/PRINTERS");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    SpoolManager() {

    }

    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + (days * 86400000L));
    }

    public static Date addHours(Date date, int hours) {
        return new Date(date.getTime() + (hours * 3600000L));
    }

    public static File[] getSpools() {
        File dir = new File(SPOOLS_DIRECTORY.toString());
        return dir.listFiles((d, name) -> (name.endsWith(".spl") || name.endsWith(".SPL")));
    }

    public static List<String> readFiles(File[] files) {
        List<String> contents = new ArrayList<>();

        for (File file : files) {
            String fileName = file.getName();
            Path filePath = Paths.get(SPOOLS_DIRECTORY.toString(), fileName);
            String content = "";
            try {
                content = Files.readString(filePath, StandardCharsets.UTF_8);
            } catch (IOException error) {
                try {
                    //System.out.println("File: " + fileName + " failed to parse UTF-8");
                    content = Files.readString(filePath, StandardCharsets.ISO_8859_1);
                } catch (IOException fallbackError) {
                    //System.out.println("File: " + fileName + " failed to parse with both UTF-8 and ISO-8859-1.");
                }
            }
            contents.add(content);
        }
        return contents;
    }

    public static List<Purchase> getPurchases(List<String> contents) {
        List<Purchase> purchases = new ArrayList<>();

        for (String content : contents) {

            if (!content.contains("DAISUSHII") && !content.contains("COMPROBANTE DE COMPRA")) {
                continue;
            }

            double total = 0;
            int id = 0;
            Date date = new Date();
            PurchaseType type = PurchaseType.CREDIT;

            // Parse date
            {
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
            }


            // Get type
            if (content.contains("P A G A D A")) {
                type = PurchaseType.CASH;
            }
            // Get id
            {
                Pattern pattern = Pattern.compile(
                        "COMPRA\\s+NUMERO:\\s+(\\d+)"
                );

                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {

                    id = Integer.parseInt(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                }
            }

            // Get total
            {
                Pattern pattern = Pattern.compile(
                        "TOTAL:\\s+\\$\\s+(.*.\\d{2})"
                );
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {

                    total = Double.parseDouble(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                }
            }


            Purchase purchase = new Purchase(total, id, date, type);
            purchases.add(purchase);

        }

        // Filter valid tickets
        List<Purchase> validPurchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if (
                    purchase != null &&
                            purchase.isValid()
                            && isWithinShift(purchase.getDate())
            ) {
                validPurchases.add(purchase);
            }
        }

        return validPurchases;
    }

    public static List<Ticket> getTickets(List<String> contents) {
        List<Ticket> tickets = new ArrayList<>();

        if (contents == null) {
            return tickets;
        }


        for (String content : contents) {

            if (!content.contains("DAISUSHII") && !content.contains("IMPTE.")) {
                continue;
            }


            String[] lines = content.split("\n");

            Date date = new Date();
            Date printed = new Date();
            boolean paid = false;
            int number = 0;
            Payment payment = new Payment();

            // Parse date
            {
                Pattern pattern = Pattern.compile("Fecha:\\s+(.*)");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    try {
                        String day = matcher.group(1);

                        Date set = dateAndTimeFormat.parse(day);

                        if (day.contains("PM")) {
                            set = addHours(set, 12);
                        }

                        date = set;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Get printed date
            {
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

                        printed = set;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (content.contains("www.grupotelnet.com.mx/facturar")) {
                paid = true;
            }

            // Get cash
            {
                Pattern pattern = Pattern.compile("EFECTIVO\\s+\\(\\$\\s+(\\S+)\\)");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    payment.set(
                            PaymentType.CASH,
                            Double.parseDouble(
                                    matcher.group(1).replace(",", "").replace(" ", "")
                            )
                    );
                }
            }

            // Get AFIRME
            {
                Pattern pattern = Pattern.compile(
                        "AFIRME:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})"
                );
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    double total = Double.parseDouble(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                    double tips = Double.parseDouble(matcher.group(2)
                            .replace(",", "")
                            .replace(" ", ""));

                    payment.set(
                            PaymentType.AFIRME,
                            (total + tips)
                    );
                }
            }

            // Get BBVA
            {
                Pattern pattern = Pattern.compile(
                        "BANCOMER:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})"
                );
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    double total = Double.parseDouble(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                    double tips = Double.parseDouble(matcher.group(2)
                            .replace(",", "")
                            .replace(" ", ""));

                    payment.set(
                            PaymentType.BBVA,
                            (total + tips)
                    );
                }
            }

            // Get RAPPI
            {
                Pattern pattern = Pattern.compile(
                        "RAPPI:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})"
                );
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    double total = Double.parseDouble(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                    double tips = Double.parseDouble(matcher.group(2)
                            .replace(",", "")
                            .replace(" ", ""));

                    payment.set(
                            PaymentType.RAPPI,
                            total + tips
                    );
                }
            }


            // Get UBER
            {
                Pattern pattern = Pattern.compile(
                        "UBER:\\s+\\$\\s+(.*.\\d{2})\\s+PROPINA:\\s+\\$\\s+(.*.\\d{2})"
                );
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    double total = Double.parseDouble(matcher.group(1)
                            .replace(",", "")
                            .replace(" ", ""));
                    double tips = Double.parseDouble(matcher.group(2)
                            .replace(",", "")
                            .replace(" ", ""));

                    payment.set(
                            PaymentType.UBER,
                            (total + tips)
                    );
                }
            }


            // Get check
            {
                Pattern pattern = Pattern.compile("POR COBRAR\\s+\\(\\$\\s+(\\S+)\\)");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    payment.set(
                            PaymentType.CHECK,
                            Double.parseDouble(
                                    matcher.group(1).replace(",", "").replace(" ", "")
                            )
                    );
                }
            }

            // Parse ticket number

            {
                for (String line : lines) {
                    if (
                            line.contains("DOMICILIO")
                                    || line.contains("LLEVAR:")
                                    || line.contains("Comedor")
                                    || line.contains("COMEDOR NO:")
                    ) {
                        String substring = line.substring(line.length() - 6);

                        number = Integer.parseInt(substring);
                    }
                }
            }
            // Get Total if not found
            {
                if (payment.total() <= 0) {
                    Pattern pattern = Pattern.compile(
                            "TOTAL\\s+\\$\\s+(.*.\\d{2})"
                    );
                    Matcher matcher = pattern.matcher(content);
                    if (matcher.find()) {
                        double total = Double.parseDouble(matcher.group(1)
                                .replace(",", "")
                                .replace(" ", ""));

                        payment.set(
                                PaymentType.CASH,
                                total
                        );
                    }
                }
            }

            Ticket ticket = new Ticket(paid, payment, number, date, printed);
            tickets.add(ticket);
        }


        // Filter valid tickets
        List<Ticket> validTickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (
                    ticket != null &&
                            ticket.isValid()
                            && isFromShift(ticket)
            ) {
                validTickets.add(ticket);
            }
        }

        return validTickets;
    }

    public static boolean isWithinShift(Date date) {
        long start;
        long end;

        Date now = new Date();
        Date fromStart = new Date(now.getTime());

        if (isEarlyMorning(fromStart)) {
            fromStart = addDays(fromStart, -1);
        }
        fromStart.setHours(7);
        fromStart.setMinutes(0);
        fromStart.setSeconds(0);

        start = fromStart.getTime();
        end = start + 57600000L;

        return date.getTime() >= start && date.getTime() <= end;
    }

    public static boolean isFromShift(Ticket ticket) {
        return isWithinShift(ticket.getDate());
    }

    public static boolean isEarlyMorning(Date date) {
        return date.getHours() < 6;
    }

    public static boolean isAfternoon(Date date) {
        return date.getHours() >= 7;
    }
}
