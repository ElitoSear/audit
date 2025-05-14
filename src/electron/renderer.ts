declare global {
    interface Window {
        spoolAPI: any;
    }
}

const getSpoolsButton = document.getElementById('getSpoolsButton') as HTMLButtonElement;
const getSpoolsButtonText = document.getElementById('getSpoolsButtonText') as HTMLParagraphElement;

getSpoolsButton.addEventListener('click', () => {
    console.log('HOLA');

    const spools = window.spoolAPI.getSpools();

    spools.forEach((spool: any, index: number) => {
        const spoolText = document.createElement('p');
        spoolText.textContent = `Spool ${index + 1}: ${spool}`;
        document.body.appendChild(spoolText);
    });
});