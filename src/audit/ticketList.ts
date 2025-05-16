import { Ticket } from './ticket.js';

export class TicketList {
    private _tickets: Ticket[] = [];
    private static invalid = Ticket.invalid();

    constructor(tickets: Ticket[] = []) {

        tickets.forEach((ticket) => {
            this.add(ticket);
        });
    }

    getTicket(number: number)
        : Ticket | undefined {
        return this.tickets.find((ticket) => {
            if (ticket.number == number) {
                return true;
            }
            return false;
        })
        //|| Ticket.invalid();
    }

    add(ticket: Ticket) {
        const duplicated = this.getTicket(ticket.number);

        console.log(duplicated);
        if (duplicated instanceof Ticket) {
            duplicated.update(ticket);
            //console.log(`Updated ticket: ${duplicated.toString()}`);
            return false;
        } else {
            //console.log(`Added ticket: ${ticket.toString()}`);
            this.tickets.push(ticket);
            return true;
        }
    }

    get tickets() {
        return this._tickets;
    }
}