import { useMemo, useState } from "react";
import { useTranslations } from "next-intl";

export function useCalendar(initialDate: Date) {
    const t = useTranslations("appointment");

    const [displayedMonth, setDisplayedMonth] = useState<Date>(() => new Date(initialDate.getFullYear(), initialDate.getMonth(), 1));

    const daysLeft = useMemo(() => {
        const now = new Date();
        const year = displayedMonth.getFullYear();
        const month = displayedMonth.getMonth();
        const lastDay = new Date(year, month + 1, 0).getDate();

        const startDay = (year === now.getFullYear() && month === now.getMonth()) ? now.getDate() : 1;

        const result: { dayNumber: number; weekday: string; date: Date }[] = [];
        for (let d = startDay; d <= lastDay; d++) {
            const current = new Date(year, month, d);
            result.push({
                dayNumber: d,
                weekday: t(`days.${current.getDay()}`),
                date: current,
            });
        }
        
        return result;
    }, [displayedMonth, t]);

    const goPrev = () => setDisplayedMonth((prev) => new Date(prev.getFullYear(), prev.getMonth() - 1, 1));
    const goNext = () => setDisplayedMonth((prev) => new Date(prev.getFullYear(), prev.getMonth() + 1, 1));

    return { displayedMonth, daysLeft, goPrev, goNext };
}