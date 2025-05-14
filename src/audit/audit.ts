import { SpoolManager } from './spool_manager.js';
import { TicketList } from './ticketList.js';

export class Audit {
    private ticketList = new TicketList();

    private getSpoolsButton: HTMLButtonElement;
    private getSpoolsButtonText: HTMLParagraphElement;

    constructor() {
        this.getSpoolsButton = document.getElementById('getSpoolsButton') as HTMLButtonElement;
        this.getSpoolsButtonText = document.getElementById('getSpoolsButtonText') as HTMLParagraphElement;

        this.getSpoolsButton.addEventListener('click', () =>{
            const spools = SpoolManager.getSpools();

            spools.forEach((spool, index) => {
                console.log('HOLA');
                const spoolText = document.createElement('p');
                spoolText.textContent = `Spool ${index + 1}: ${spool}`;
                document.body.appendChild(spoolText);
            });
        });
    }
}