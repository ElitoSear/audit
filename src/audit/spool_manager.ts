import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import { unzipSync } from 'zlib';
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

            const texts = zipEntries.map((entry) => {
                return entry.entryName;
            });

        //     return texts;

            // const fpageEntry = zipEntries.find(entry =>
            //     entry.entryName.includes('Documents/1/Pages/1.fpage') &&
            //     !entry.isDirectory
            // );

            // if (!fpageEntry) {
            //     console.warn(`No 1.fpage found in ${file}`);
            //     return [];
            // }

            // // Look for string under /Documents/1/Pages/1.fpage

            // const fileContent = fpageEntry.getData().toString('utf-8');
            
            // const fpageRegex = /UnicodeString="([^"]+)"/g;
            // const fpageMatches = fileContent.match(fpageRegex);
            // const fpageTexts = fpageMatches ? fpageMatches.map(match => match.slice(14, -1)) : [];

            // return fpageTexts;
        });

        return extractedTexts;
    }
}