package edward.audit;

import edward.audit.audit.*;
import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    public Label realCashTotal;
    @FXML
    public Label lastUpdate;
    @FXML
    public Label calpishitos;
    @FXML
    private TextArea cashText;

    @FXML
    private Label cashTotal;

    @FXML
    private TextArea rappiText;

    @FXML
    private Label rappiTotal;

    @FXML
    private TextArea afirmeText;

    @FXML
    private Label afirmeTotal;

    @FXML
    private TextArea bbvaText;

    @FXML
    private Label bbvaTotal;

    @FXML
    private TextArea uberText;

    @FXML
    private Label uberTotal;

    @FXML
    private TextArea checkText;

    @FXML
    private Label checkTotal;
    @FXML
    private TextArea purchasesCreditText;

    @FXML
    private Label purchasesCreditTotal;

    @FXML
    private TextArea purchasesCashText;

    @FXML
    private Label purchasesCashTotal;

    @FXML
    protected void onUpdateButtonClick() {
        update();
    }

    @FXML
    protected void onLessPreciseUpdateButtonClick() {
        update(true);
    }
    @FXML
    void update() {
        update(false);
    }

    void listTotals(TextArea textArea, Label label, TicketList ticketList, PaymentType type) {
        StringBuilder content = new StringBuilder();

        for (Ticket ticket : ticketList.getTickets(type)) {
            content.append(ticket.format(type)).append("\n");
        }

        textArea.setText(content.toString());
        label.setText("Total: $" + String.format("%.2f" , ticketList.getTotal(type)));
    }

    void listTotals(TextArea textArea, Label label, PurchaseList purchaseList, PurchaseType type) {
        StringBuilder content = new StringBuilder();

        for (Purchase purchase : purchaseList.getPurchases(type)) {
            content.append(purchase.format()).append("\n");
        }

        textArea.setText(content.toString());
        label.setText("Total: $" + String.format("%.2f" , purchaseList.getTotal(type)));
    }

    void update(boolean strict) {

        Date now = new Date();

        lastUpdate.setText("Última actualización: " + SpoolManager.dayDateAndTimeFormat.format(now));

        File[] files = SpoolManager.getSpools();

        List<String> contents = SpoolManager.readFiles(files);

        List<Ticket> rawTickets = SpoolManager.getTickets(contents);
        List<Purchase> purchases = SpoolManager.getPurchases(contents);

        List<Ticket> tickets = rawTickets;

        if (strict) {
            tickets = new ArrayList<>(rawTickets.stream().filter(Ticket::isPaid).toList());
        }

        TicketList ticketList = new TicketList(tickets);
        PurchaseList purchaseList = new PurchaseList(purchases);

        int calpishitosQ = ticketList.getTickets().stream().mapToInt(Ticket::getCalpishitos).sum();

        calpishitos.setText("Calpishitos: " + calpishitosQ);

        listTotals(cashText, cashTotal, ticketList, PaymentType.CASH);
        listTotals(rappiText, rappiTotal, ticketList, PaymentType.RAPPI);
        listTotals(afirmeText, afirmeTotal, ticketList, PaymentType.AFIRME);
        listTotals(bbvaText, bbvaTotal, ticketList, PaymentType.BBVA);
        listTotals(uberText, uberTotal, ticketList, PaymentType.UBER);
        listTotals(checkText, checkTotal, ticketList, PaymentType.CHECK);

        listTotals(purchasesCashText, purchasesCashTotal, purchaseList, PurchaseType.CASH);
        listTotals(purchasesCreditText, purchasesCreditTotal, purchaseList, PurchaseType.CREDIT);

        double cash = ticketList.getTotal(PaymentType.CASH) - purchaseList.getTotal(PurchaseType.CASH);

        realCashTotal.setText("$" + String.format("%.2f", cash));
    }
}
