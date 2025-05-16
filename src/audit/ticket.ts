export enum TicketType {
    RAPPI, UBER, CASH, AFIRME, BBVA, TRANSFER
}

export class Ticket {
    private _date: Date;
    private _printed: Date;
    private _number: number;
    private _paid: boolean;
    private _total: number;
    private _type: TicketType;

    constructor(paid: boolean, total: number, number: number, date: Date, printed: Date, type: TicketType = TicketType.CASH) {
        this._paid = paid;
        this._total = total;
        this._type = type;
        this._number = number;
        this._date = date;
        this._printed = printed;
    }

    set date(date: Date) {
        this._date = date;
    }

    set printed(printed: Date) {
        this._printed = printed;
    }

    set number(number: number) {
        this._number = number;
    }

    set paid(paid: boolean) {
        this._paid = paid;
    }

    set total(total: number) {
        this._total = total;
    }

    set type(type: TicketType) {
        this._type = type;
    }

    get date() {
        return this._date;
    }

    get printed() {
        return this._printed;
    }

    get number() {
        return this._number;
    }

    get paid() {
        return this._paid;
    }

    get total() {
        return this._total;
    }

    get type() {
        return this._type;
    }

    public toString = (): string => {
        return `Folio: ${this.number}, Total: ${this.total}`;
    }

    public update(ticket: Ticket) {
        if (ticket.printed.getTime() < this.printed.getTime()) return this;

        return new Ticket(ticket.paid, ticket.total, ticket.number, ticket.date, ticket.printed, ticket.type);
    }

    static log() {
        console.log;
    }

}