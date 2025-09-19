"use client"

import { Calendar, MoveLeft, MoveRight } from "lucide-react";
import { useTranslations } from "next-intl";
import './appointment.css'
import { useEffect, useMemo, useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { AppointmentData } from "./Appointment";

type Props = {
    appointmentFormData: AppointmentData;
    setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
};

export default function DateTime({ appointmentFormData, setAppointmentFormData }: Props) {

    const t = useTranslations('appointment');

    function formatLocalDateForBackend(date: Date): string {
        const tzOffsetMs = date.getTimezoneOffset() * 60000;
        return new Date(date.getTime() - tzOffsetMs).toISOString().slice(0, 19);
    }

    function roundUpToNext30Min(date: Date): Date {
        const rounded = new Date(date);
        const minutes = rounded.getMinutes();
        const next = Math.ceil(minutes / 30) * 9;
        rounded.setSeconds(0, 0);
        rounded.setMinutes(next);
        return rounded;
    }

    const [displayedMonth, setDisplayedMonth] = useState<Date>(() => {
        const d = new Date(appointmentFormData.date);
        return new Date(d.getFullYear(), d.getMonth(), 1);
    });

    const daysLeft = useMemo(() => {
        const now  = new Date();
        const year = displayedMonth.getFullYear();
        const month = displayedMonth.getMonth();
        const lastDay = new Date(year, month + 1, 0).getDate();

        const startDay = year === now.getFullYear() && month === now.getMonth() ? now.getDate() : 1;

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

    const { mutate, data, error, isPending } = useMutation({
        mutationFn: async () => {
            const payload = {
                appointmentDate: formatLocalDateForBackend(appointmentFormData.date),
                services: appointmentFormData.services,
                details: appointmentFormData.details,
            };

            const res = await fetch("http://localhost:8081/api/appointment/time-slots", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (!res.ok) {
                throw new Error("Failed to fetch slots");
            }
            return res.json();
        },
    });

    useEffect(() => {
        mutate();
    }, []);

    const goPrev = () => setDisplayedMonth((prev) => new Date(prev.getFullYear(), prev.getMonth() - 1, 1));
    const goNext = () => setDisplayedMonth((prev) => new Date(prev.getFullYear(), prev.getMonth() + 1, 1));

    function handleDayClick(dayNumber: number) {
        const year = displayedMonth.getFullYear();
        const month = displayedMonth.getMonth();
        const clickedDate = new Date(year, month, dayNumber);

        const today = new Date();
        const isToday =
        clickedDate.getFullYear() === today.getFullYear() &&
        clickedDate.getMonth() === today.getMonth() &&
        clickedDate.getDate() === today.getDate();

        let finalDate: Date;
        if (isToday) {
            // Round "now" up to next slot
            finalDate = roundUpToNext30Min(new Date());
            // If rounding put us earlier than start of working hours, move to start
            if (finalDate.getHours() < 9) {
                finalDate = new Date(finalDate.getFullYear(), finalDate.getMonth(), finalDate.getDate(), 9, 0, 0);
            }
        } else {
            // For other days start at 09:00
            finalDate = new Date(year, month, dayNumber, 9, 0, 0);
        }

        // Update parent state so other parts of UI know the selected date/time
        setAppointmentFormData(prev => ({ ...prev, date: finalDate }));

        mutate();
    }

    function isSelectedDay(dayNumber: number) {
        const sel = new Date(appointmentFormData.date);
        return (
            sel.getFullYear() === displayedMonth.getFullYear() &&
            sel.getMonth() === displayedMonth.getMonth() &&
            sel.getDate() === dayNumber
        );
    }
    
    return (
        <main className="flex flex-col w-full h-full min-h-0 min-w-0 pt-4 px-2 gap-y-6">
            <div className="flex items-center justify-between gap-x-4">
                <div className="flex gap-x-2 items-center justify-center">
                    <Calendar color="var(--orange)" />
                    <span>{t(`months.${displayedMonth.getMonth()}`)} {displayedMonth.getFullYear()}</span>
                </div>
                <div className="flex gap-x-2 items-center justify-center">
                    <MoveLeft onClick={goPrev} size={28} />
                    <MoveRight onClick={goNext} size={28} />
                </div>
            </div>
            <div className="w-full overflow-x-auto scrollbar-hide">
                <div className="flex gap-x-4 flex-nowrap">
                    {daysLeft.map(({ dayNumber, weekday }) => (
                        <button key={dayNumber} onClick={() => handleDayClick(dayNumber)} className="flex flex-col items-center justify-center gap-y-2">
                            <div className={`flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center cursor-pointer ${isSelectedDay(dayNumber) ? 'bg-[var(--orange)] text-white' : 'bg-white text-black'}`}>
                                {dayNumber}
                            </div>
                            <span className="text-sm">{weekday}</span>
                        </button>
                    ))}
                </div>
            </div>
            <div className="flex flex-1 flex-col pt-6 gap-y-6 overflow-y-auto scrollbar-hide">
                {(!isPending && data) && data.map((timeSlot: any, index: number) => (
                    <div key={index} className="flex flex-col gap-y-3">
                        <div className="flex justify-between items-center">
                            <span className="font-bold">{timeSlot}</span>
                            <input type="radio" name="time" className="accent-[var(--orange)] cursor-pointer" />
                        </div>
                        <hr />
                    </div>
                ))}
            </div>
        </main>
    );
}