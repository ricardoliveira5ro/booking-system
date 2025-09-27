import { useMutation } from "@tanstack/react-query";
import { useTranslations } from "next-intl";

import { AppointmentData } from "@/models/Appointment";
import { isToday, roundUpToNext30Min } from "@/utils/functions";

export function useTimeSlots(appointmentFormData: AppointmentData) {

    const errors = useTranslations('errors');

    return useMutation({
        mutationFn: async () => {
            try {
                const payload = {
                    appointmentDate: appointmentFormData.date.toISOString().split('T')[0],
                    appointmentTime: appointmentFormData.time || (isToday(appointmentFormData.date) ? roundUpToNext30Min().time : "09:00"),
                    services: appointmentFormData.services.map(s => s.code)
                };
    
                const res = await fetch("http://localhost:8081/api/appointment/time-slots", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload),
                });

                const data = await res.json().catch(() => ({}));

                if (!res.ok) 
                    throw new Error(errors(data?.code || 'GENERAL'));

                return data;

            } catch (err) {
                if (err instanceof TypeError) // Network Error
                    throw new Error(errors('apiNoService'));
                
                throw err;
            }
        },
        retry: 0
    })
}