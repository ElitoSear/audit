import { readdirSync, readFileSync } from 'fs';
import { join } from 'path';
import AdmZip from 'adm-zip';

export class SpoolManager {
    static spoolerDir = 'C:\\Windows\\System32\\spool\\PRINTERS';
    static pageFolder = 'Documents/1/Pages/1.fpage';

    static getSpools() {
        const files = readdirSync(this.spoolerDir);
        const spoolFiles = files.filter(file => {
            return (file.endsWith('.spl') || file.endsWith('.SPL'));
        });

        const extractedTexts = spoolFiles.map(file => {

            const filePath = join(this.spoolerDir, file);
            const buffer = readFileSync(filePath);

            try {
                const zip = new AdmZip(buffer);
                const zipEntries = zip.getEntries();

                const rootIsFolder = zipEntries.some((entry) => {
                    return (
                        entry.entryName.startsWith(this.pageFolder) &&
                        entry.entryName != this.pageFolder
                    );
                });

                const entries = zipEntries.filter((entry) => {
                    entry.entryName.startsWith(this.pageFolder);

                    if (rootIsFolder || entry.entryName == this.pageFolder) {
                        return true;
                    }
                });

                const uniString = entries.map((entry) => {
                    return zip.readFile(entry)?.toString('utf-8');
                });

                const content = uniString.join();

                if (content == null) {
                    return '';
                }

                const regex = /UnicodeString="([^"]+)"/g;
                const matches = content.match(regex);
                const texts = matches ? matches.map(match => match.slice(15, -1)) : [];
                const fpage = texts.join('');

                return fpage;

            } catch (errror) {

                const regex = /Fecha:([^"]+)\n/g;
                const matches = buffer.toString().match(regex);


                

            }


        });

        return extractedTexts;
    }
}