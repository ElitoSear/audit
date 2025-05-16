declare global {
    interface Window {
        spoolAPI: any;
    }
}

const refresh = document.getElementById('refresh') as HTMLButtonElement;
const activity = document.getElementById('activity') as HTMLDivElement;
const fersita = document.getElementById('fersita') as HTMLHeadingElement;

const date = new Date();

if (
    (date.getDay() >= 1 && date.getDay() <= 4) &&
    (date.getHours() === 17 && date.getMinutes() >= 45) ||
    (date.getHours() === 18 && date.getMinutes() <= 15)
) {
    fersita.textContent = 'Hola, mi amor. Bienvenida a tu corte. :)';
}

refresh.addEventListener('click', () => {
    const tickets = window.spoolAPI.getSpools();
    activity.textContent = '';

    tickets.forEach((ticket: any, index: number) => {
        const li = document.createElement('li');
        li.textContent = `${ticket}`;
        activity.appendChild(li);
    });
});