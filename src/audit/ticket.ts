export enum TicketType {
    RAPPI, UBER, CASH, AFIRME, BBVA, TRANSFER
}

export class Ticket {
    private isPaid: boolean;
    private total: number;
    private type: TicketType

    constructor(isPaid: boolean, total: number, type: TicketType = TicketType.CASH) {
        this.isPaid = isPaid;
        this.total = total;
        this.type = type;
    }

    static fromXML(xml: string) {

    }

}