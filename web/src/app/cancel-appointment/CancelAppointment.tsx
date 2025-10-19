'use client'

import APICallError from '@/components/APICallError';
import { AppointmentData } from '@/models/Appointment';
import { useQuery } from '@tanstack/react-query';
import { CalendarX, MoveLeft } from 'lucide-react';
import { useTranslations } from 'next-intl';
import { useRouter, useSearchParams } from 'next/navigation'
import { useEffect, useState } from 'react';
import { Slide, toast } from 'react-toastify';
import CancelAppointmentSkeleton from './CancelAppointmentSkeleton';
import { useCancelAppointment } from '@/hooks/useCancelAppointment';

export default function CancelAppointment() {

    const t = useTranslations('appointment');
    const errors = useTranslations('errors');
    
    const router = useRouter();  

    const searchParams = useSearchParams();
    const appointmentId = searchParams.get('appointment-id');
    const cancelKey = searchParams.get('cancel-key');

    const [appointmentData, setAppointmentData] = useState<AppointmentData>();

    const { data, isPending, error, isError, refetch } = useQuery({
        queryKey: ['getAppointment'],
        queryFn: async () => {
            try {
                const res = await fetch(`${process.env.NEXT_PUBLIC_API_BASE}/appointment/${appointmentId}`)
                const data = await res.json();

                if (data.code === 'APPOINTMENT_NOT_FOUND')
                    router.replace('/');
    
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
    });

    useEffect(() => {
        if (data) {
            const appointmentDate = new Date(data.appointmentDate);

            setAppointmentData({
                services: data.services.map((s: { code: string; name: string; price: number; slotTime: string }) => ({
                    code: s.code,
                    name: s.name,
                    price: s.price,
                    slotTime: String(s.slotTime),
                })),
                date: appointmentDate,
                time: appointmentDate.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
                details: data.details,
            });
        }
    }, [data]);

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

    const { mutate } = useCancelAppointment(appointmentId ?? '', cancelKey ?? '');
    const handleCancelAppointment = () => {
        mutate(undefined, {
            onSuccess: () => {
                router.replace('/');
                toast.success(t("cancelAppointmentSuccess"), {
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
            },
            onError: (err) => {
                router.replace('/');
                toast.error(err.message, {
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
        });
    };

    const renderContent = () => {
        if (isPending) {
            return <CancelAppointmentSkeleton />;
        }

        if (isError) {
            return <APICallError retry={refetch} />;
        }

        if (!appointmentData) return null;

        const dayKey = `days.${appointmentData.date.getDay()}`;
        const dayLabel = t(dayKey);
        const monthKey = `months.${appointmentData.date.getMonth()}`;
        const monthLabel = t(monthKey);
        const formattedDate = `${appointmentData.date.getDate()} ${monthLabel}, ${dayLabel}, ${appointmentData.time}`;

        return (
            <div className='flex flex-col gap-y-8'>
                <div className='border rounded-lg px-8 py-2'>
                    <span className='text-white'>{formattedDate}</span>
                </div>
                <div className='border rounded-lg px-2 py-1'>
                    {appointmentData.services.map((service: { code: string; name: string; price: number; slotTime: string }, index: number) => (
                        <div key={service.code}>
                            {index > 0 && <hr className="text-gray-500" /> }
                            <div className="flex justify-between items-center rounded-lg gap-x-8 px-3 py-1.5">
                                <div className="flex flex-col">
                                    <span>{t(`services.${service.code}`)}</span>
                                    <span className="text-sm text-gray-400">{service.slotTime} min</span>
                                </div>
                                <span>{service.price} €</span>
                            </div>
                        </div>
                    ))}
                </div>
                <div className='flex flex-col gap-y-4'>
                    <button onClick={handleCancelAppointment} className='flex gap-x-2 justify-center items-center bg-[var(--orange)] px-4 py-1.5 rounded-lg cursor-pointer'>
                        <span className='text-lg'>{t('cancelAppointmentBtn')}</span>
                        <CalendarX />
                    </button>
                    <button onClick={() => router.replace('/')} className='flex gap-x-2 justify-center items-center px-4 py-1.5 rounded-lg cursor-pointer'>
                        <MoveLeft />
                        <span className='text-lg'>{t('cancelAppointmentBack')}</span>
                    </button>
                </div>
            </div>
        );
    };

    return (
        <div className='flex flex-col items-center h-screen px-8 py-16 gap-y-12'>
            <h1 className='text-xl text-center'>{t('cancelAppointmentTitle')}</h1>
            {renderContent()}
        </div>
    );
}