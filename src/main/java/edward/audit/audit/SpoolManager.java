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

import static edward.audit.audit.Utils.*;


public class SpoolManager {
    public static final Path SPOOLS_DIRECTORY = Path.of("C:/Windows/System32/spool/PRINTERS");

    SpoolManager() {

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
                    content = Files.readString(filePath, StandardCharsets.ISO_8859_1);
                } catch (IOException fallbackError) {
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

            Purchase purchase = new Purchase(content);
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

            int calpishitos = 0;

            Pattern pattern = Pattern.compile("(\\d+)\\s+PIE\\s+CALPISHITO\\s+BOT");
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {

                calpishitos += Integer.parseInt(matcher.group(1)
                        .replace(",", "")
                        .replace(" ", ""));

            }

            Ticket ticket = new Ticket(content);

            ticket.setCalpishitos(calpishitos);

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


}
