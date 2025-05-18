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

    @Override
    public String toString() {
        return value;
    }
}
