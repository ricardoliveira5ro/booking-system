"use client"

import { Calendar, MoveLeft, MoveRight } from "lucide-react";
import { useTranslations } from "next-intl";
import './dateTime.css'
import { useMemo, useState } from "react";

export default function DateTime() {

    const t = useTranslations('appointment');

    const [date, setDate] = useState(new Date());
    const daysLeft = useMemo(() => {
        const today = date;
        const year = today.getFullYear();
        const month = today.getMonth();

        const lastDay = new Date(year, month + 1, 0).getDate();
        const result = [];

        for (let d = today.getDate(); d <= lastDay; d++) {
            const current = new Date(year, month, d);
            result.push({
                dayNumber: d,
                weekday: t(`days.${current.getDay()}`),
            });
        }

        return result;
    }, []);
    
    return (
        <main className="flex flex-col w-full h-full min-h-0 min-w-0 pt-4 px-2 gap-y-6">
            <div className="flex items-center justify-between gap-x-4">
                <div className="flex gap-x-2 items-center justify-center">
                    <Calendar color="var(--orange)" />
                    <span>{t(`months.${date.getMonth()}`)}</span>
                </div>
                <div className="flex gap-x-2 items-center justify-center">
                    <MoveLeft size={28} />
                    <MoveRight size={28} />
                </div>
            </div>
            <div className="w-full overflow-x-auto scrollbar-hide">
                <div className="flex gap-x-4 flex-nowrap">
                    {daysLeft.map(({ dayNumber, weekday }) => (
                        <div key={dayNumber} className="flex flex-col items-center justify-center gap-y-2">
                            <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">
                                {dayNumber}
                            </div>
                            <span className="text-sm">{weekday}</span>
                        </div>
                    ))}
                </div>
            </div>
            <div className="flex flex-1 flex-col pt-6 gap-y-6 overflow-y-auto scrollbar-hide">
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">9:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">9:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">10:00</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">10:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">11:00</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">11:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">12:00</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">12:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
                <div className="flex flex-col gap-y-3">
                    <div className="flex justify-between items-center">
                        <span className="font-bold">13:30</span>
                        <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                    </div>
                    <hr />
                </div>
            </div>
        </main>
    );
}