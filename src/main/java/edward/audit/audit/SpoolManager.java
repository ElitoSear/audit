package edward.audit.audit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
                    //System.out.println("Reading file: " + fileName);


                    Path filePath = Paths.get(SPOOLS_DIRECTORY.toString(), fileName);
                    String content = "";
                    try {
                        content = Files.readString(filePath);
                    } catch (MalformedInputException error) {
                        System.out.println("File: " + fileName + " failed to parse.");
                        continue;
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
                    double total = 0;
                    TicketType type = TicketType.CASH;

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

                    // Parse printed time
                    for (String line : lines) {
                        if (line.contains("Imp:")) {
                            String time = line.substring(line.length() - 11).trim();
                            try {

                                Date set = dateAndTimeFormat.parse(dateFormat.format(date) + " " + time);
                                if (line.contains("PM")) {
                                    set = addHours(set, 12);
                                }
                                printed = set;
                            }  catch (ParseException e) {
                                e.printStackTrace();
                            }

                            break;
                        }
                    }

                    // Check if paid
                    for (String line : lines) {
                        if (line.contains("www")) {
                            paid = true;
                            break;
                        }
                    }

                    // Check ticket type
                    for (String line : lines) {
                        //System.out.println("Type:" + line);
                        if (line.contains("AFIRME")) {
                            type = TicketType.AFIRME;
                            paid = true;
                            break;
                        } else if (line.contains("BANCOMER")) {
                            type = TicketType.BBVA;
                            paid = true;
                            break;
                        } else if (line.contains("RAPPI")) {
                            type = TicketType.RAPPI;
                            paid = true;
                            break;
                        }
                    }

                    // Parse ticket number
                    for (String line : lines) {
                        if (
                                line.contains("DOMICILIO")
                                        || line.contains("LLEVAR")
                                            || line.contains("Comedor")
                        ) {
                            String substring = line.substring(line.length() - 6);

                            number = Integer.parseInt(substring);
                            break;
                        }
                    }

                    // Parse total
                    for (String line : lines) {
                        if (line.contains("TOTAL")) {
                            Pattern pattern = Pattern.compile("TOTAL\\s+\\$\\s+(\\d+\\.\\d{2})");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                total = Double.parseDouble(matcher.group(1));
                                break;
                            }
                        }
                    }
                    //System.out.println(total);
                    Ticket ticket = new Ticket(paid, total, number, date, printed, type);

                    //System.out.println("Folio: " + number + ", Total: " + total + ", Date: " + date + ", Printed On: " + printed);
                    parsedTickets.add(ticket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println(parsedTickets);

        // Filter valid tickets
        List<Ticket> validTickets = new ArrayList<>();
        for (Ticket ticket : parsedTickets) {
            if (
                    ticket != null &&
                            ticket.isValid()
                                && ticket.getType() == TicketType.CASH
                                    && isFromShift(ticket)
            ) {
                //System.out.println(ticket);
                validTickets.add(ticket);
            }
        }

        return validTickets;
    }

    private static Date getLastModifiedTime(File file) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return new Date(attrs.lastModifiedTime().toMillis());
        } catch (IOException e) {
            return new Date();
        }
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
        end = start + 57600000L; // 16 hours in milliseconds

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
