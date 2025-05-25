package edward.audit.audit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final SimpleDateFormat dayDateAndTimeFormat = new SimpleDateFormat("EE dd/MM/yyyy hh:mm:ss aa");

    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + (days * 86400000L));
    }

    public static Date addHours(Date date, int hours) {
        return new Date(date.getTime() + (hours * 3600000L));
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

    public static String regex(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";

    }

}
