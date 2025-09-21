export function isToday(currentDate: Date): boolean {
    const today = new Date();

    return currentDate.getFullYear() === today.getFullYear() &&
            currentDate.getMonth() === today.getMonth() &&
            currentDate.getDate() === today.getDate();
}

export function roundUpToNext30Min(): { date: Date; time: string } {
    const now = new Date();
    const rounded = new Date(now);
    rounded.setMinutes(Math.ceil(now.getMinutes() / 30) * 30, 0, 0);

    if (rounded.getHours() < 9) {
        rounded.setHours(9, 0, 0, 0);
    }

    const hours = String(rounded.getHours()).padStart(2, "0");
    const minutes = String(rounded.getMinutes()).padStart(2, "0");
    const time = `${hours}:${minutes}`;

    return { date: rounded, time };
}

export function formatLocalDateForBackend(date: Date): string {
    const tzOffsetMs = date.getTimezoneOffset() * 60000;

    return new Date(date.getTime() - tzOffsetMs).toISOString().slice(0, 19);
}