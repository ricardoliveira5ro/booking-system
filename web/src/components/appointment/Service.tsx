"use client"

import { useQuery } from '@tanstack/react-query'
import { Slide, toast } from 'react-toastify';
import { useEffect } from 'react';
import { useTranslations } from 'next-intl';

import { ServiceSkeleton } from './ServiceSkeleton';
import APICallError from '../APICallError';

import { AppointmentData } from '@/models/Appointment';

export default function Service({ appointmentFormData, setAppointmentFormData }: {
    appointmentFormData: AppointmentData;
    setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
}) {

    const t = useTranslations('appointment');
    const errors = useTranslations('errors');
    
    const { data = [], isPending, error, isError, refetch } = useQuery({
        queryKey: ['services'],
        queryFn: async () => {
            try {
                const res = await fetch('http://localhost:8081/api/appointment/services')

                if (!res.ok)
                    throw new Error(errors(`${res.status}`));

                return res.json();

            } catch (err: any) {
                throw new Error(errors('general'))
            }
        },
        retry: 0
    })

    useEffect(() => {
        if (isError) {
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
    }, [isError])

    const toggleService = (service: { code: string; name: string; price: number; slotTime: string }, checked: boolean) => {
        setAppointmentFormData((prev) => ({
            ...prev,
            services: checked ? [...prev.services, service]: prev.services.filter((s) => s.code !== service.code),
        }));
    };

    return (
        <main className="flex flex-col w-full px-2 pt-8 gap-y-8">
            {isPending ? 
                <ServiceSkeleton /> : 
                isError ?
                    <APICallError retry={refetch} /> :
                    data.map((service: any, index: number) => (
                        <div key={service.code} data-aos="fade-right" data-aos-duration="900" data-aos-delay={`${index * 100}`}>
                            <div className="flex pb-4 gap-x-4">
                                <div className="flex flex-col w-full gap-y-0.5">
                                    <h3 className="font-sans text-lg">{t(`services.${service.code}`)}</h3>
                                    <span className="text-sm text-gray-300">{service.slotTime} mins</span>
                                </div>
                                <input checked={appointmentFormData.services.some(s => s.code === service.code)} onChange={(e) => toggleService(service, e.target.checked)} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                            </div>
                            <hr className="text-gray-500" />
                        </div>
                    ))
            }
        </main>
    );
}