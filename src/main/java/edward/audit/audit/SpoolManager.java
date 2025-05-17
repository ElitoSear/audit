package edward.audit.audit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.MalformedInputException;
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

    public static List<Ticket> getTickets() {
        List<Ticket> parsedTickets = new ArrayList<>();
        File dir = new File(SPOOLS_DIRECTORY.toString());
        File[] files = dir.listFiles();

        if (files == null) {
            return parsedTickets;
        }

        for (File file : files) {
            String fileName = file.getName();
            if (
                    (fileName.endsWith(".spl") || fileName.endsWith(".SPL"))
            ) {
                try {
                    Path filePath = Paths.get(SPOOLS_DIRECTORY.toString(), fileName);
                    String content = "";
                    try {
                        // Attempt to read the file as UTF-8
                        content = Files.readString(filePath, StandardCharsets.UTF_8);
                    } catch (MalformedInputException error) {
                        // Fallback to ISO-8859-1 if UTF-8 fails
                        try {
                            content = Files.readString(filePath, StandardCharsets.ISO_8859_1);
                        } catch (MalformedInputException fallbackError) {
                            System.out.println("File: " + fileName + " failed to parse with both UTF-8 and ISO-8859-1.");
                            continue;
                        }
                    }

                    if (!content.contains("DAISUSHII") && !content.contains("IMPTE.")) {
                        continue;
                    }

                    String[] lines = content.split("\\r?\\n");

                    if (lines.length == 1 || lines.length == 0) {
                        continue;
                    }

                    Date date = new Date();
                    Date printed = new Date();
                    boolean paid = false;
                    int number = 0;
                    Payment payment = new Payment();

                    // Parse date
                    for (String line : lines) {
                        if (line.contains("Fecha:")) {

                            String dateString = line.substring(line.length() - 22).trim();

                            String from = dateString.substring(0, dateString.length() - 2).trim();

                            try {
                                Date set = dateAndTimeFormat.parse(from);
                                if (line.contains("PM")) {
                                    set = addHours(set, 12);
                                }
                                date = set;
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    }

                    // Get printed date
                    for (String line : lines) {
                        if (line.contains("Imp:")) {
                            Pattern pattern = Pattern.compile("Imp:\\s+(\\S+)\\s+(\\S+)");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                try {
                                    String time = matcher.group(1);
                                    Date set = dateAndTimeFormat.parse(dateFormat.format(date) + " " + time);
                                    if (line.contains("PM")) {
                                        set = addHours(set, 12);
                                    }
                                    printed = set;
                                }  catch (ParseException e) {
                                    System.out.println(file);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    if (content.contains("www")) {
                        paid = true;
                    }

                    // Get cash
                    for (String line : lines) {
                        if (line.contains("EFECTIVO")) {
                            Pattern pattern = Pattern.compile("EFECTIVO\\s+\\(\\$\\s+(\\S+)\\)");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.CASH,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }

                    // Get AFIRME
                    for (String line : lines) {
                        if (line.contains("AFIRME")) {
                            Pattern pattern = Pattern.compile("AFIRME:\\s+\\$\\s+(.*.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.AFIRME,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }

                    // Get BBVA
                    for (String line : lines) {
                        if (line.contains("BANCOMER")) {
                            Pattern pattern = Pattern.compile("BANCOMER:\\s+\\$\\s+(.*.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.BBVA,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }

                    // Get RAPPI
                    for (String line : lines) {
                        if (line.contains("RAPPI")) {
                            Pattern pattern = Pattern.compile("RAPPI:\\s+\\$\\s+(.*.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.RAPPI,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }


                    // Get UBER
                    for (String line : lines) {
                        if (line.contains("UBER")) {
                            Pattern pattern = Pattern.compile("UBER:\\s+\\$\\s+(.*.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.UBER,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }

                    // Get CHECK
                    for (String line : lines) {
                        if (line.contains("CHEQ")) {
                            Pattern pattern = Pattern.compile("CHEQ\\s+\\(\\$\\s+(\\S+)\\)");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.set(
                                        PaymentType.CHECK,
                                        Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", ""))
                                );
                                break;
                            }
                        }
                    }

                    // Parse ticket number
                    for (String line : lines) {
                        if (
                                line.contains("DOMICILIO")
                                        || line.contains("LLEVAR")
                                                || line.contains("Comedor")
                                                        || line.contains("COMEDOR")
                        ) {
                            String substring = line.substring(line.length() - 6);

                            number = Integer.parseInt(substring);
                            break;
                        }
                    }

                    // Get AFIRME
                    for (String line : lines) {
                        if (line.contains("TOTAL") && payment.total() == 0) {
                            Pattern pattern = Pattern.compile("TOTAL\\s+\\$\\s+(.*.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                payment.setCash(Double.parseDouble(matcher.group(1).replace( ",", "").replace(" ", "")));
                                break;
                            }
                        }
                    }

                    Ticket ticket = new Ticket(paid, payment, number, date, printed);

                    parsedTickets.add(ticket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // Filter valid tickets
        List<Ticket> validTickets = new ArrayList<>();
        for (Ticket ticket : parsedTickets) {
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
