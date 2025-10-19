import { useMutation } from "@tanstack/react-query";
import { useTranslations } from "next-intl";

export function useCancelAppointment(appointmentId: string, cancelKey: string) {

    const errors = useTranslations('errors');

    return useMutation({
        mutationFn: async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/appointment/${appointmentId}?cancelKey=${cancelKey}`, { method: "DELETE" });

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