import { useMutation } from "@tanstack/react-query";
import { useTranslations } from "next-intl";

import { AppointmentData } from "@/models/Appointment";

export function useCreateAppointment(appointmentFormData: AppointmentData) {

    const errors = useTranslations('errors');

    return useMutation({
        mutationFn: async () => {
            try {
                const payload = {
                    appointmentDate: appointmentFormData.date.toISOString().split('T')[0],
                    appointmentTime: appointmentFormData.time,
                    services: appointmentFormData.services.map(s => s.code),
                    details: appointmentFormData.details,
                };

                const res = await fetch("http://localhost:8081/api/appointment", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload),
                });

                const data = await res.json();
    
                if (!res.ok)
                    throw new Error(errors(`${data.code || 'GENERAL'}`));
    
                return data;

            } catch (err) {
                if (err instanceof TypeError)
                    throw new Error(errors('apiNoService'));
                
                throw err;
            }
        }
    });
}