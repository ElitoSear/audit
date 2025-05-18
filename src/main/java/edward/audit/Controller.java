package edward.audit;

import edward.audit.audit.SpoolManager;
import edward.audit.audit.Ticket;
import edward.audit.audit.TicketList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class Controller {
    @FXML
    private Label ticketsText;

    @FXML
    protected void onUpdateButtonClick() {

        List<Ticket> tickets = SpoolManager.getTickets();
        TicketList ticketList = new TicketList(tickets);
        String ticketString = ticketList.toString();
        ticketsText.setText(ticketString + "\n\n\n" + "Total: " + ticketList.getTotal());
    }
}