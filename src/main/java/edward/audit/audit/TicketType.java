package edward.audit.audit;

public enum TicketType {
    RAPPI("RAPPI"),
    UBER("UBER"),
    CASH("CASH"),
    AFIRME("AFIRME"),
    BBVA("BBVA"),
    CHECK("CHECK");

    private final String value;

    TicketType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TicketType getFromBaucher(int id) {
        switch (id) {
            case 1:
                return BBVA;
            case 3:
                return RAPPI;
            case 4:
                return UBER;
            default:
                return AFIRME;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
