import { Ticket } from './ticket.js';

export class TicketList {
    private _tickets: Ticket[];
    private static invalid = Ticket.invalid();

    constructor(tickets: Ticket[] = []) {
        this._tickets = tickets;

        tickets.forEach((ticket) => {
            this.add(ticket);
        });
    }

    getTicket(number: number): Ticket | undefined {
        return this.tickets.find((ticket) => {
            if (ticket.number == number) {
                return true;
            }

            return false;
        });
    }

    add(ticket: Ticket) {
        const duplicated = this.getTicket(ticket.number);

        if (duplicated instanceof Ticket) {
            duplicated.update(ticket);
            return false;
        } else {
            this.tickets.push(ticket);
            return true;
        }
    }
    
    get tickets() {
        return this._tickets;
    }
}