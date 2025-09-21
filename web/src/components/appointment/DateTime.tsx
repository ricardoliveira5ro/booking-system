"use client"

import { useTranslations } from "next-intl";
import { useEffect } from "react";

import { DateTimeCalendarHeader } from "@/components/appointment/DateTimeCalendarHeader";
import { DateTimeDaySelector } from "@/components/appointment/DateTimeDaySelector";
import { DateTimeSlotSkeleton } from "./DateTimeSlotSkeleton";

import { useTimeSlots } from "@/hooks/useTimeSlots";
import { useCalendar } from "@/hooks/useCalendar";

import { isToday, roundUpToNext30Min } from "@/utils/functions";
import { AppointmentData } from "@/models/Appointment";

import '../../app/appointment/appointment.css'

export default function DateTime({ appointmentFormData, setAppointmentFormData }: {
    appointmentFormData: AppointmentData;
    setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
}) {

    const t = useTranslations('appointment');

    const { displayedMonth, daysLeft, goPrev, goNext } = useCalendar(appointmentFormData.date);
    const { mutate, data, isPending } = useTimeSlots(appointmentFormData);

    useEffect(() => {
        mutate();
    }, [appointmentFormData.date]);

    function handleDayClick(clickedDate: Date) {
        let finalDate: Date;
        
        if (isToday(clickedDate)) {
            const rounded = roundUpToNext30Min();
            finalDate = new Date(rounded.date.getFullYear(), rounded.date.getMonth(), rounded.date.getDate(), Number(rounded.time.split(':')[0]), 0, 0);
        } else {
            finalDate = new Date(clickedDate.getFullYear(), clickedDate.getMonth(), clickedDate.getDate(), 9, 0, 0);
        }
        
        setAppointmentFormData(prev => ({ ...prev, date: finalDate, time: null }));
    }

    function isSelectedDay(day: Date) {
        const sel = new Date(appointmentFormData.date);
        return (
            sel.getFullYear() === day.getFullYear() &&
            sel.getMonth() === day.getMonth() &&
            sel.getDate() === day.getDate()
        );
    }
    
    return (
        <main className="flex flex-col w-full h-full min-h-0 min-w-0 pt-4 px-2 gap-y-6">
            <DateTimeCalendarHeader 
                monthLabel={`${t(`months.${displayedMonth.getMonth()}`)} ${displayedMonth.getFullYear()}`}
                onPrev={goPrev}
                onNext={goNext}
            />
            <DateTimeDaySelector 
                days={daysLeft} 
                onDayClick={handleDayClick} 
                isSelected={isSelectedDay} 
            />
            <div className="flex flex-1 flex-col pt-6 gap-y-6 overflow-y-auto scrollbar-hide">
                {(isPending || !data) ?
                    <DateTimeSlotSkeleton /> : 
                    data.map((timeSlot: any, index: number) => (
                        <div key={index} className="flex flex-col gap-y-3 cursor-pointer" onClick={() => setAppointmentFormData((prev) => ({ ...prev, time: timeSlot }))}>
                            <div className="flex justify-between items-center">
                                <span className="font-bold">{timeSlot}</span>
                                <input type="radio" name="time" className="accent-[var(--orange)] pointer-events-none" checked={appointmentFormData.time === timeSlot} readOnly />
                            </div>
                            <hr />
                        </div>
                    ))
                }
            </div>
        </main>
    );
}