export function roundUpToNext30Min(): Date {
    const rounded = new Date();
    rounded.setMinutes(Math.ceil(rounded.getMinutes() / 30) * 30, 0, 0);

    return rounded;
}

export function formatLocalDateForBackend(date: Date): string {
    const tzOffsetMs = date.getTimezoneOffset() * 60000;

    return new Date(date.getTime() - tzOffsetMs).toISOString().slice(0, 19);
}