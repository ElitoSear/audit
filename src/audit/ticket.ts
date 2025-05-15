export enum TicketType {
    RAPPI, UBER, CASH, AFIRME, BBVA, TRANSFER
}

export class Ticket {
    private datePrinted: Date;
    private date: Date;
    private number: number;
    private isPaid: boolean;
    private total: number;
    private type: TicketType

    constructor(isPaid: boolean, total: number, number: number, date: Date, datePrinted: Date, type: TicketType = TicketType.CASH) {
        this.isPaid = isPaid;
        this.total = total;
        this.type = type;
        this.number = number;
        this.date = date;
        this.datePrinted = date;
    }

    static fromXML(xml: string) {

    }

}