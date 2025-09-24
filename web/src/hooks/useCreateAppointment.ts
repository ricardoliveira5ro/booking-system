import { useMutation } from "@tanstack/react-query";
import { AppointmentData } from "@/models/Appointment";

export function useCreateAppointment(appointmentFormData: AppointmentData) {
    return useMutation({
        mutationFn: async () => {
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

            if (!res.ok) throw new Error("Failed to fetch slots");

            return res.json();
        }
    });
}