package edward.audit.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchaseList {
    private final List<Purchase> purchases = new ArrayList<>();

    public PurchaseList() {
        this(new ArrayList<>());
    }

    public PurchaseList(List<Purchase> purchases) {
        purchases.sort((first, second) -> Long.compare(second.getDate().getTime(), first.getDate().getTime()));

        for (Purchase purchase : purchases) {
            this.add(purchase);
        }
    }

    public void add(Purchase purchase) {
        boolean duplicated = purchases.stream().anyMatch(t -> t.getId() == purchase.getId());

        if (!duplicated) {
            purchases.add(purchase);
        }
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public List<Purchase> getPurchases(PurchaseType type) {

        if (Objects.requireNonNull(type) == PurchaseType.CASH) {
            return this.getPurchases().stream()
                    .filter(purchase -> purchase.getType() == PurchaseType.CASH)
                    .toList();
        }
        return this.getPurchases().stream()
                .filter(purchase -> purchase.getType() == PurchaseType.CREDIT)
                .toList();
    }

    public double getTotal() {
        return this.getPurchases().stream()
                .mapToDouble(Purchase::getTotal)
                .sum();
    }

    public double getTotal(PurchaseType type) {
        return this.getPurchases().stream()
                .mapToDouble(purchase -> purchase.getType() == type ? purchase.getTotal() : 0)
                .sum();
    }

    @Override
    public String toString() {
        return this.getPurchases()
                .stream()
                .map(Purchase::toString)
                .reduce("", (acc, purchase) -> acc + purchase + "\n");
    }
}