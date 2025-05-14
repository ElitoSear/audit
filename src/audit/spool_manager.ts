import { readdirSync } from 'fs';
import { readFileSync } from 'fs';
import { join } from 'path';
import { unzipSync } from 'zlib';

export class SpoolManager {

    static spoolerDir = 'C:\\Windows\\System32\\spool\\PRINTERS';

    constructor() {
    }

    static getSpools() {
        const files = readdirSync(this.spoolerDir);
        const spoolFiles = files.filter(file => file.endsWith('.spl'));

        const extractedTexts = spoolFiles.map(file => {
            const filePath = join(this.spoolerDir, file);
            const buffer = readFileSync(filePath);
            const decompressedBuffer = unzipSync(buffer);
            const fileContent = decompressedBuffer.toString('utf8');
            const regex = /UnicodeString="([^"]+)"/g;
            const matches = fileContent.match(regex);
            const texts = matches ? matches.map(match => match.slice(14, -1)) : [];
            return texts;
        });

        return extractedTexts;
    }
}