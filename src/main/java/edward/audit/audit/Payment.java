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

    double total() {
        return cash + afirme + bbva + check;
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
}
