import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import AdmZip from 'adm-zip';

export class SpoolManager {
    static spoolerDir = 'C:\\Windows\\System32\\spool\\PRINTERS';

    static getSpools() {
        const files = readdirSync(this.spoolerDir);
        const spoolFiles = files.filter(file => {
            return (file.endsWith('.spl') || file.endsWith('.SPL'));
        });

        const extractedTexts = spoolFiles.map(file => {

            const filePath = join(this.spoolerDir, file);
            const buffer = readFileSync(filePath);
            
            const zip = new AdmZip(buffer);
            const zipEntries = zip.getEntries();


            // @toDo if 1.fpage is a directory join the texts from the inside files. Else do what is below            

            const content = zip.readFile("Documents/1/Pages/1.fpage")?.toString('utf-8');

            if (content == null) {
                return '';
            }

            const fpageRegex = /UnicodeString="([^"]+)"/g;
            const fpageMatches = content.match(fpageRegex);
            const fpageTexts = fpageMatches ? fpageMatches.map(match => match.slice(15, -1)) : [];
            const fpage = fpageTexts.join('');

            return fpage;
        });

        return extractedTexts;
    }
}