import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import { Ticket, TicketType } from './ticket.js';

export class SpoolManager {
    static spoolerDir = 'C:\\Windows\\System32\\spool\\PRINTERS';
    static pageFolder = 'Documents/1/Pages/1.fpage';
    private static invalid = Ticket.invalid();

    static shiftDays(date: Date, days: number) {
        return new Date(date.getTime() + (days * (86400000)));
    }

    static getTickets(): Ticket[] {
        const files = readdirSync(this.spoolerDir);
        const spoolFiles = files.filter(file => {
            return (file.endsWith('.spl') || file.endsWith('.SPL'));
        });

        const extractedTexts = spoolFiles.map(file => {

            const filePath = join(this.spoolerDir, file);
            const buffer = readFileSync(filePath);
            const string = buffer.toString('utf-8');
            const lines = string.match(/(.*)/g) || [];

            let date = new Date();
            let printed = new Date();
            let paid = false;
            let number = 0;
            let total = 0;
            let type = TicketType.CASH;

            lines.some((line) => {
                if (!line.includes('Fecha:')) {
                    return false;
                }
                const matches = string.match(/Fecha:(.*)M/g) || [];
                let from = matches.join().substring(7);
                from = `${from.substring(3, 5)}/${from.substring(0, 2)}${from.substring(5)}`
                date = new Date(from);
                return true;
            });

            lines.some((line) => {
                if (!line.includes('Fecha:')) {
                    return false;
                }
                const matches = string.match(/Imp:(.*)M/g) || [];
                const time = matches.join().substring(6);
                printed = new Date(`${date.toLocaleDateString()} ${time}`);
                return true;
            });

            paid = lines.some((line) => {
                return line.includes('http');
            });

            lines.some((line) => {
                const matches = line.match(/--\d+/g) || [];
                const match = matches.join();

                if (match.length > 0) {
                    number = parseInt(match.substring(2));
                    return true;
                } else {
                    return false;
                }
            });

            lines.some((line) => {
                const match = line.match(/TOTAL\s*\$\s*(\d+\.\d{2})/);

                if (match && match[1]) {
                    total = parseFloat(match[1]);
                    return true;
                } else {
                    return false;
                }
            });

            const ticket = new Ticket(paid, total, number, date, printed, type);
            return ticket;
        });

        return extractedTexts.filter((ticket) => {
            if (ticket.number != 0 && ticket != undefined && this.isFromShift(ticket)) {
                return true;
            } else {
                return false;
            }
        });
    }

    static isFromShift(ticket: Ticket): boolean {
        const now = new Date();
        const ticketDate = ticket.date;

        const valid = this.isEarlyMorning(now) ? this.shiftDays(now, -1) : new Date(now);

        if (this.isAfternoon(ticketDate)) {
            if (ticketDate.toLocaleDateString() == valid.toLocaleDateString()) {
                return true;
            }
            return false;
        } else {
            if (this.shiftDays(ticketDate, -1).toLocaleDateString() == valid.toLocaleDateString()) {
                return true;
            }
            return false;
        }
    }

    static isEarlyMorning(date: Date) {
        return date.getHours() < 6;
    }

    static isAfternoon(date: Date) {
        return date.getHours() >= 12;
    }
}
