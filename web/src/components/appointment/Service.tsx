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
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/appointment/services`)
                const data = await res.json();
    
                if (!res.ok)
                    throw new Error(errors(`${data.code || 'GENERAL'}`));
    
                return data;

            } catch (err) {
                if (err instanceof TypeError) // Network Error
                    throw new Error(errors('apiNoService'));
                
                throw err;
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
    }, [isError, error?.message])

    const toggleService = (service: { code: string; name: string; price: number; slotTime: string }, checked: boolean) => {
        setAppointmentFormData((prev) => ({
            ...prev,
            services: checked ? [...prev.services, service]: prev.services.filter((s) => s.code !== service.code),
        }));
    };

    const renderContent = () => {
        if (isPending) {
            return <ServiceSkeleton />;
        }

        if (isError) {
            return <APICallError retry={refetch} />;
        }

        return data.map((service: { code: string; name: string; price: number; slotTime: string }, index: number) => (
            <div key={service.code} data-aos="fade-right" data-aos-duration="900" data-aos-delay={`${index * 100}`}>
                <div className="flex pb-4 gap-x-4">
                    <div className="flex flex-col w-full gap-y-0.5">
                        <h3 className="font-sans text-lg">{t(`services.${service.code}`)}</h3>
                        <span className="text-sm text-gray-300">{service.slotTime} mins</span>
                    </div>
                    <input checked={appointmentFormData.services.some((s) => s.code === service.code)} onChange={(e) => toggleService(service, e.target.checked)} type="checkbox" className="accent-[var(--orange)] cursor-pointer"/>
                </div>
                <hr className="text-gray-500" />
            </div>
        ));
    };

    return (
        <main className="flex flex-col w-full px-2 pt-8 gap-y-8">
            {renderContent()}
        </main>
    );
}