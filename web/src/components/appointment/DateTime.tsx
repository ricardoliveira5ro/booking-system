"use client"

import { useTranslations } from "next-intl";
import { useEffect } from "react";
import { Slide, toast } from "react-toastify";
import { SearchX } from "lucide-react";

import { DateTimeCalendarHeader } from "@/components/appointment/DateTimeCalendarHeader";
import { DateTimeDaySelector } from "@/components/appointment/DateTimeDaySelector";
import { DateTimeSlotSkeleton } from "./DateTimeSlotSkeleton";
import APICallError from "../APICallError";

import { useTimeSlots } from "@/hooks/useTimeSlots";
import { useCalendar } from "@/hooks/useCalendar";

import { AppointmentData } from "@/models/Appointment";

import '../../app/appointment/appointment.css'

interface DateTimeProps {
    readonly appointmentFormData: AppointmentData;
    readonly setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
}

export default function DateTime({ appointmentFormData, setAppointmentFormData }: DateTimeProps) {

    const t = useTranslations('appointment');

    const { displayedMonth, daysLeft, goPrev, goNext } = useCalendar(appointmentFormData.date);
    const { mutate, data = [], isPending, isError, error } = useTimeSlots(appointmentFormData);

    const monthKey = `months.${displayedMonth.getMonth()}`;
    const monthLabel = t(monthKey);

    useEffect(() => {
        mutate();
    }, [appointmentFormData.date, mutate]);

    function handleDayClick(clickedDate: Date) {
        const finalDate = new Date(clickedDate.getFullYear(), clickedDate.getMonth(), clickedDate.getDate(), 9, 0, 0); // Time is irrelevant but fixes timezone issue (day before)
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

    useEffect(() => {
        if (isError) {
            console.log("DateTime")
            console.log(error)
            toast.error(error.message, {
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: false,
                pauseOnHover: false,
                draggable: true,
                progress: undefined,
                theme: "dark",
                transition: Slide,
            });
        }
    }, [isError, error])

    const handleTimeSlotClick = (timeSlot: string) => {
        setAppointmentFormData((prev) => ({
            ...prev,
            time: timeSlot,
        }));
    };

    const renderContent = () => {
        if (isPending) {
            return <DateTimeSlotSkeleton />;
        }

        if (isError) {
            return <APICallError retry={mutate} />;
        }

        if (data.length === 0) {
            return (
                <div className='flex w-full items-center gap-x-2'>
                    <SearchX color='#FE5F55' />
                    <span>{t('noTimeSlotsAvailable')}</span>
                </div>
            );
        }

        return data.map((timeSlot: string) => (
            <button key={timeSlot} className="flex flex-col gap-y-3 cursor-pointer" onClick={() => handleTimeSlotClick(timeSlot)}>
                <div className="flex justify-between items-center">
                    <span className="font-bold">{timeSlot}</span>
                    <input type="radio" name="time" className="accent-[var(--orange)] pointer-events-none" checked={appointmentFormData.time === timeSlot} readOnly />
                </div>
                <hr />
            </button>
        ));
    };
    
    return (
        <main className="flex flex-col w-full h-full min-h-0 min-w-0 pt-4 px-2 gap-y-6">
            <DateTimeCalendarHeader 
                monthLabel={`${monthLabel} ${displayedMonth.getFullYear()}`}
                onPrev={goPrev}
                onNext={goNext}
            />
            <DateTimeDaySelector 
                days={daysLeft} 
                onDayClick={handleDayClick} 
                isSelected={isSelectedDay} 
            />
            <div className="flex flex-1 flex-col pt-6 gap-y-6 overflow-y-auto scrollbar-hide">
                {renderContent()}
            </div>
        </main>
    );
}