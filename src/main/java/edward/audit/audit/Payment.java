package edward.audit.audit;

public class Payment {
    private double cash;
    private double afirme;
    private double bbva;
    private double check;
    private double rappi;
    private double ubber;

    Payment() {
        this(0, 0, 0, 0);
    }

    Payment(double cash) {
        this(cash, 0, 0, 0);
    }

    Payment(double cash, double afirme) {
        this(cash, afirme, 0, 0);
    }

    Payment(double cash, double afirme, double bbva) {
        this(cash, afirme, bbva, 0);
    }

    Payment(double cash, double afirme, double bbva, double check) {
        this.cash = cash;
        this.afirme = afirme;
        this.bbva = bbva;
        this.check = check;
    }

    Payment of() {
        return new Payment(0, 0, 0, 0);
    }

    Payment of(double cash) {
        return new Payment(cash, 0, 0, 0);
    }

    Payment of(double cash, double afirme) {
        return new Payment(cash, afirme, 0, 0);
    }

    Payment of(double cash, double afirme, double bbva) {
        return new Payment(cash, afirme, bbva, 0);
    }

    Payment of(double cash, double afirme, double bbva, double check) {
        return new Payment(cash, afirme, bbva, check);
    }


    double add(double amount, PaymentType type) {
        switch (type) {
            case RAPPI:
                rappi += amount;
                return (cash - amount);
            case UBER:
                ubber += amount;
                return (cash - amount);
            case AFIRME:
                afirme += amount;
                return (cash - amount);
            case BBVA:
                bbva += amount;
                return (cash - amount);
            case CHECK:
                check += amount;
                return (cash - amount);
            default:
                cash += amount;
                return (cash - amount);
        }
    }

    void set(PaymentType type, double amount) {
        switch (type) {
            case RAPPI:
                rappi = amount;
                break;
            case UBER:
                ubber = amount;
                break;
            case AFIRME:
                afirme = amount;
                break;
            case BBVA:
                bbva = amount;
                break;
            case CHECK:
                check = amount;
                break;
            default:
                cash = amount;
                break;
        }
    }

    double get(PaymentType type) {
        switch (type) {
            case RAPPI:
               return rappi;
            case UBER:
               return ubber;
            case AFIRME:
                return afirme;
            case BBVA:
              return bbva;
            case CHECK:
               return check;
            default:
              return cash;
        }
    }

    public double total() {
        return cash + afirme + bbva + check + rappi + ubber;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public void setAfirme(double afirme) {
        this.afirme = afirme;
    }

    public void setBBVA(double bbva) {
        this.bbva = bbva;
    }

    public void setCheck(double check) {
        this.check = check;
    }

    public void setRappi(double rappi) {
        this.rappi = rappi;
    }

    public void setUber(double uber) {
        this.ubber = uber;
    }

    double getCash() {
        return cash;
    }

    double getAfirme() {
        return afirme;
    }

    double getBBVA() {
        return bbva;
    }

    double getCheck() {
        return check;
    }

    double getRappi() {
        return rappi;
    }

    double getUber() {
        return ubber;
    }

    @Override
    public String toString() {
        return "Pago: {" +
                "Efectivo: " + cash +
                ", Afirme: " + afirme +
                ", BBVA: " + bbva +
                ", Transferencias: " + check +
                ", Rappi: " + rappi +
                ", Ubber: " + ubber +
                '}';
    }
}
