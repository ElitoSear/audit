package edward.audit;

import edward.audit.audit.*;
import javafx.fxml.FXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class Controller {
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

        File[] files = SpoolManager.getSpools();

        List<String> contents = SpoolManager.readFiles(files);

        List<Ticket> tickets = SpoolManager.getTickets(contents);
        List<Purchase> purchases = SpoolManager.getPurchases(contents);

        {
            TicketList ticketList = new TicketList(tickets);

            String content = "";

            for (Ticket ticket : ticketList.getTickets(PaymentType.CASH)) {
                content += (ticket.format(PaymentType.CASH) + "\n");
            }

            cashText.setText(content);
            cashTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.CASH)));

        }

        {
            TicketList ticketList = new TicketList(tickets);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.RAPPI)) {
                content += (ticket.format(PaymentType.RAPPI) + "\n");
            }

            rappiText.setText(content);
            rappiTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.RAPPI)));
        }

        {

            TicketList ticketList = new TicketList(tickets);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.AFIRME)) {
                content += (ticket.format(PaymentType.AFIRME) + "\n");
            }

            afirmeText.setText(content);
            afirmeTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.AFIRME)));
        }

        {

            TicketList ticketList = new TicketList(tickets);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.BBVA)) {
                content += (ticket.format(PaymentType.BBVA) + "\n");
            }

            bbvaText.setText(content);
            bbvaTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.BBVA)));
        }

        {
            TicketList ticketList = new TicketList(tickets);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.UBER)) {
                content += (ticket.format(PaymentType.UBER) + "\n");
            }

            uberText.setText(content);
            uberTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.UBER)));
        }

        {
            TicketList ticketList = new TicketList(tickets);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.CHECK)) {
                content += (ticket.format(PaymentType.CHECK) + "\n");
            }

            checkText.setText(content);
            checkTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.CHECK)));
        }

        {
            PurchaseList purchaseList = new PurchaseList(purchases);

            String content = "";
            for (Purchase purchase : purchaseList.getPurchases(PurchaseType.CASH)) {
                content += (purchase.format() + "\n");
            }

            purchasesCashText.setText(content);
            purchasesCashTotal.setText("Total: " + String.format("%.2f" , purchaseList.getTotal(PurchaseType.CASH)));
        }

        {
            PurchaseList purchaseList = new PurchaseList(purchases);

            String content = "";
            for (Purchase purchase : purchaseList.getPurchases(PurchaseType.CREDIT)) {
                content += (purchase.format() + "\n");
            }

            purchasesCreditText.setText(content);
            purchasesCreditTotal.setText("Total: " + String.format("%.2f" , purchaseList.getTotal()));
        }
    }

    @FXML
    protected void onLessPreciseUpdateButtonClick() {

        File[] files = SpoolManager.getSpools();

        List<String> contents = SpoolManager.readFiles(files);

        List<Ticket> rawTickets = SpoolManager.getTickets(contents);
        List<Purchase> purchases = SpoolManager.getPurchases(contents);

        List<Ticket> tickets = new ArrayList<>(
                rawTickets.stream().filter(Ticket::isPaid).toList()
        );

        {
            TicketList ticketList = new TicketList(tickets, true);

            String content = "";

            for (Ticket ticket : ticketList.getTickets(PaymentType.CASH)) {
                content += (ticket.format(PaymentType.CASH) + "\n");
            }

            cashText.setText(content);
            cashTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.CASH)));

        }

        {
            TicketList ticketList = new TicketList(tickets, true);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.RAPPI)) {
                content += (ticket.format(PaymentType.RAPPI) + "\n");
            }

            rappiText.setText(content);
            rappiTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.RAPPI)));
        }

        {

            TicketList ticketList = new TicketList(tickets, true);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.AFIRME)) {
                content += (ticket.format(PaymentType.AFIRME) + "\n");
            }

            afirmeText.setText(content);
            afirmeTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.AFIRME)));
        }

        {

            TicketList ticketList = new TicketList(tickets, true);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.BBVA)) {
                content += (ticket.format(PaymentType.BBVA) + "\n");
            }

            bbvaText.setText(content);
            bbvaTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.BBVA)));
        }

        {
            TicketList ticketList = new TicketList(tickets, true);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.UBER)) {
                content += (ticket.format(PaymentType.UBER) + "\n");
            }

            uberText.setText(content);
            uberTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.UBER)));
        }

        {
            TicketList ticketList = new TicketList(tickets, true);

            String content = "";
            for (Ticket ticket : ticketList.getTickets(PaymentType.CHECK)) {
                content += (ticket.format(PaymentType.CHECK) + "\n");
            }

            checkText.setText(content);
            checkTotal.setText("Total: " + String.format("%.2f" , ticketList.getTotal(PaymentType.CHECK)));
        }

        {
            PurchaseList purchaseList = new PurchaseList(purchases);

            String content = "";
            for (Purchase purchase : purchaseList.getPurchases(PurchaseType.CASH)) {
                content += (purchase.format() + "\n");
            }

            purchasesCashText.setText(content);
            purchasesCashTotal.setText("Total: " + String.format("%.2f" , purchaseList.getTotal(PurchaseType.CASH)));
        }

        {
            PurchaseList purchaseList = new PurchaseList(purchases);

            String content = "";
            for (Purchase purchase : purchaseList.getPurchases(PurchaseType.CREDIT)) {
                content += (purchase.format() + "\n");
            }

            purchasesCreditText.setText(content);
            purchasesCreditTotal.setText("Total: " + String.format("%.2f" , purchaseList.getTotal()));
        }
    }
}
