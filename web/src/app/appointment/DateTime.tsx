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

    useEffect(() => {
        console.log(appointmentFormData)
    }, [])

    const { mutate, data, error, isPending } = useMutation({
        mutationFn: async () => {
        const body = {
            appointmentDay: "2025-09-15T00:00:00",
            services: ["HC"],
            details: {
                name: "Ricardo",
                email: "ricardo@gmail.com",
                phoneNumber: "123456789",
                message: "Just want something fresh"
            }
        };

        const res = await fetch("http://localhost:8081/api/appointment/time-slots", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body),
        });

        if (!res.ok) {
            throw new Error("Failed to fetch slots");
        }
        return res.json();
        },
    });

    useEffect(() => {
        mutate();
    }, [mutate]);
    
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