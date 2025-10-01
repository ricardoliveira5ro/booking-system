import { useMutation } from "@tanstack/react-query";
import { useTranslations } from "next-intl";

import { AppointmentData } from "@/models/Appointment";

export function useTimeSlots(appointmentFormData: AppointmentData) {

    const errors = useTranslations('errors');

    return useMutation({
        mutationFn: async () => {
            try {
                const payload = {
                    appointmentDate: appointmentFormData.date.toISOString().split('T')[0],
                    services: appointmentFormData.services.map(s => s.code)
                };
    
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/appointment/time-slots`, {
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