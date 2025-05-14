import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import { unzipSync } from 'zlib';

export class SpoolManager {
    static spoolerDir = 'C:\\Windows\\System32\\spool\\PRINTERS';

    static getSpools() {
        const files = readdirSync(this.spoolerDir);
        const spoolFiles = files.filter(file => {
            return (file.endsWith('.spl') || file.endsWith('.SPL'));
        });

        const extractedTexts = spoolFiles.map(file => {
            const filePath = join(this.spoolerDir, file);
            // const buffer = readFileSync(filePath);
            // const decompressedBuffer = unzipSync(buffer);
            // const fileContent = decompressedBuffer.toString('utf8');

            // // Look for string under /Documents/1/Pages/1.fpage
            // const fpagePath = '/Documents/1/Pages/1.fpage';
            // const fpageContent = fileContent.substring(fileContent.indexOf(fpagePath));
            // const fpageRegex = /UnicodeString="([^"]+)"/g;
            // const fpageMatches = fpageContent.match(fpageRegex);
            // const fpageTexts = fpageMatches ? fpageMatches.map(match => match.slice(14, -1)) : [];

            return filePath;
        });

        return extractedTexts;
    }
}