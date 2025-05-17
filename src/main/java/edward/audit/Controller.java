package edward.audit;

import edward.audit.audit.PaymentType;
import edward.audit.audit.SpoolManager;
import edward.audit.audit.Ticket;
import edward.audit.audit.TicketList;
import javafx.fxml.FXML;

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
    protected void onCashButtonClick() {

        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";

        for (Ticket ticket : ticketList.getTickets(PaymentType.CASH)) {
            content += (ticket + "\n");
        }

        cashText.setText(content + "\n\n\n");
        cashTotal.setText("Total: " + ticketList.getTotal(PaymentType.CASH));
    }

    @FXML
    protected void onRappiButtonClick() {
        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";
        for (Ticket ticket : ticketList.getTickets(PaymentType.RAPPI)) {
            content += (ticket + "\n");
        }

        rappiText.setText(content + "\n\n\n");
        rappiTotal.setText("Total: " + ticketList.getTotal(PaymentType.RAPPI));
    }

    @FXML
    protected void onAfirmeButtonClick() {
        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";
        for (Ticket ticket : ticketList.getTickets(PaymentType.AFIRME)) {
            content += (ticket + "\n");
        }

        afirmeText.setText(content + "\n\n\n");
        afirmeTotal.setText("Total: " + ticketList.getTotal(PaymentType.AFIRME));
    }

    @FXML
    protected void onBbvaButtonClick() {
        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";
        for (Ticket ticket : ticketList.getTickets(PaymentType.BBVA)) {
            content += (ticket + "\n");
        }

        bbvaText.setText(content + "\n\n\n");
        bbvaTotal.setText("Total: " + ticketList.getTotal(PaymentType.BBVA));
    }

    @FXML
    protected void onUberButtonClick() {
        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";
        for (Ticket ticket : ticketList.getTickets(PaymentType.UBER)) {
            content += (ticket + "\n");
        }

        uberText.setText(content + "\n\n\n");
        uberTotal.setText("Total: " + ticketList.getTotal(PaymentType.UBER));
    }

    @FXML
    protected void onCheckButtonClick() {
        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);

        String content = "";
        for (Ticket ticket : ticketList.getTickets(PaymentType.CHECK)) {
            content += (ticket + "\n");
        }

        checkText.setText(content + "\n\n\n");
        checkTotal.setText("Total: " + ticketList.getTotal(PaymentType.CHECK));
    }
}
