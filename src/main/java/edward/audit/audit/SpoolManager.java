package edward.audit.audit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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
                    String content = Files.readString(filePath);
                    String[] lines = content.split("\\r?\\n");

                    if (lines.length == 0) {
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
                            Pattern pattern = Pattern.compile("Fecha:(.*)M");
                            Matcher matcher = pattern.matcher(content);
                            if (matcher.find()) {
                                String from = matcher.group(1).trim();
                                from = from.substring(3, 5) + "/" + from.substring(0, 2) + from.substring(5);
                                try {
                                    date = dateAndTimeFormat.parse(from);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                    }

                    // Parse printed time
                    for (String line : lines) {
                        if (line.contains("Fecha:")) {
                            Pattern pattern = Pattern.compile("Imp:(.*)M");
                            Matcher matcher = pattern.matcher(content);
                            if (matcher.find()) {
                                String time = matcher.group(1).trim();
                                try {
                                    printed = dateAndTimeFormat.parse(dateAndTimeFormat.format(date) + " " + time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                    }

                    // Check if paid
                    for (String line : lines) {
                        if (line.contains("http")) {
                            paid = true;
                            break;
                        }
                    }

                    // Check ticket type
                    for (String line : lines) {
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
                        Pattern pattern = Pattern.compile("--\\d+");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String match = matcher.group();
                            number = Integer.parseInt(match.substring(2));
                            break;
                        }
                    }

                    // Parse total
                    for (String line : lines) {
                        Pattern pattern = Pattern.compile("TOTAL\\s*\\$\\s*(\\d+\\.\\d{2})");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            total = Double.parseDouble(matcher.group(1));
                            break;
                        }
                    }
                    Ticket ticket = new Ticket(paid, total, number, date, printed, type);
                    parsedTickets.add(ticket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Filter valid tickets
        List<Ticket> validTickets = new ArrayList<>();
        for (Ticket ticket : parsedTickets) {
            if (ticket != null && ticket.isValid()
                    //&& isFromShift(ticket)
            ) {
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
        Long start;
        Long end;

        Date fromStart = new Date(date.getTime());

        if (isEarlyMorning(date)) {
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
