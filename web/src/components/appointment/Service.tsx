"use client"

import { AppointmentData } from '@/models/Appointment';
import { useQuery } from '@tanstack/react-query'

type Props = {
    appointmentFormData: AppointmentData;
    setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
};

export default function Service({ appointmentFormData, setAppointmentFormData }: Props) {

    const { data, isPending, error } = useQuery({
        queryKey: ['services'],
        queryFn: () => fetch('http://localhost:8081/api/appointment/services').then(r => r.json()),
    })

    const toggleService = (code: string, checked: boolean) => {
        setAppointmentFormData((prev) => ({
            ...prev,
            services: checked ? [...prev.services, code]: prev.services.filter((s) => s !== code),
        }));
    };

    return (
        <main className="flex flex-col w-full px-2 pt-8 gap-y-8">
            {!isPending && data.map((s: any, index: number) => (
                <div key={s.code} data-aos="fade-right" data-aos-duration="900" data-aos-delay={`${index * 100}`}>
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-0.5">
                            <h3 className="font-sans text-lg">{s.name}</h3>
                            <span className="text-sm text-gray-300">{s.slotTime} mins</span>
                        </div>
                        <input checked={appointmentFormData.services.includes(s.code)} onChange={(e) => toggleService(s.code, e.target.checked)} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                    </div>
                    <hr className="text-gray-500" />
                </div>
            ))}
        </main>
    );
}