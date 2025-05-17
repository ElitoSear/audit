package edward.audit.audit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TicketList {
    private final List<Ticket> tickets = new ArrayList<>();

    public TicketList() {
        this(new ArrayList<>());
    }

    public TicketList(List<Ticket> tickets) {
        tickets.sort(new Comparator<Ticket>() {
            @Override
            public int compare(Ticket first, Ticket second) {
                return Long.compare(second.getPrinted().getTime(), first.getPrinted().getTime());
            }
        });

        for (Ticket ticket : tickets) {
            this.add(ticket);
        }
    }

    public Ticket getTicket(int number) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == number) {
                return ticket;
            }
        }
        return null;
    }

    public void add(Ticket ticket) {
        boolean duplicated = tickets.stream().anyMatch(t -> t.getId() == ticket.getId());

        if (duplicated) {
            //System.out.println("Duplicated ticket: " + ticket);
        } else {
            //System.out.println("Added ticket: " + ticket);
            tickets.add(ticket);
        }
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public List<Ticket> getTickets(PaymentType type) {
        return this.getTickets().stream()
                .filter(ticket -> ticket.getPayment().get(type) > 0)
                .toList();
    }

    public double getTotal() {
        return this.getTickets().stream()
                .mapToDouble(ticket -> ticket.getPayment().total())
                .sum();
    }

    public double getTotal(PaymentType type) {
        return this.getTickets().stream()
                .mapToDouble(ticket -> ticket.getPayment().get(type))
                .sum();
    }

    @Override
    public String toString() {
        return this.getTickets()
                .stream()
                .map(Ticket::toString)
                .reduce("", (acc, ticket) -> acc + ticket + "\n");
    }
}