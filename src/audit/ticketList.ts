import { Ticket } from './ticket.js';

export class TicketList {
    private _tickets: Ticket[];
    private temp = new Ticket(false, 0, 0, new Date(), new Date());

    constructor(tickets: Ticket[] = []) {
        this._tickets = []

        tickets.forEach((ticket) => {
            this.add(ticket);
        });
    }

    get tickets() {
        return this._tickets;
    }

    getTicket(number: number) {
        return this.tickets.find((ticket) => {
            if (ticket.number == number) {
                return true;
            } 

            return false;
        });
    }

    add(ticket: Ticket) {
        const duplicated = this.tickets.some((t) => {
            return t.number == ticket.number;
        });

        if (duplicated) {
            let ticketToUpdate = this.getTicket(ticket.number);
            
            if (ticketToUpdate == null) {
                return false;
            }

            ticketToUpdate = ticketToUpdate.update(ticket);
            return false;
        } else {
            this.tickets.push(ticket);
            return true;
        }
    }
}