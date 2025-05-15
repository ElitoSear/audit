declare global {
    interface Window {
        spoolAPI: any;
    }
}

const getSpoolsButton = document.getElementById('getSpoolsButton') as HTMLButtonElement;
const getSpoolsButtonText = document.getElementById('getSpoolsButtonText') as HTMLDivElement;

getSpoolsButton.addEventListener('click', () => {
    const spools = window.spoolAPI.getSpools();
    console.log(spools);
    // getSpoolsButtonText.textContent = '';

    // spools.forEach((spool: any, index: number) => {
    //     const spoolText = document.createElement('p');
    //     spoolText.textContent = `${spool}`;
    //     getSpoolsButtonText.appendChild(spoolText);
    // });
});