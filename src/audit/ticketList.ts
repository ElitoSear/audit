import { Ticket } from './ticket.js';

export class TicketList {
    private tickets: Ticket[] = [];
    private ticketCount: number = 0;

    constructor() {
        this.tickets = [];
        this.ticketCount = 0;
    }
}