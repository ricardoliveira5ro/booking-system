import { useMutation } from "@tanstack/react-query";

import { AppointmentData } from "@/models/Appointment";
import { isToday, roundUpToNext30Min } from "@/utils/functions";

export function useTimeSlots(appointmentFormData: AppointmentData) {
    return useMutation({
        mutationFn: async () => {
            const payload = {
                appointmentDate: appointmentFormData.date.toISOString().split('T')[0],
                appointmentTime: appointmentFormData.time || (isToday(appointmentFormData.date) ? roundUpToNext30Min().time : "09:00"),
                services: appointmentFormData.services,
                details: appointmentFormData.details,
            };

            const res = await fetch("http://localhost:8081/api/appointment/time-slots", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload),
            });

            if (!res.ok) throw new Error("Failed to fetch slots");

            return res.json();
        },
    })
}