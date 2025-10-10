import { useTranslations } from 'next-intl';
import { AppointmentData } from '@/models/Appointment';

import '../../app/appointment/appointment.css';

interface DetailsProps {
    readonly appointmentFormData: AppointmentData;
    readonly setAppointmentFormData: React.Dispatch<React.SetStateAction<AppointmentData>>;
}

export default function Details({ appointmentFormData, setAppointmentFormData }: DetailsProps) {

    const t = useTranslations('appointment');

    return (
        <main className="flex flex-col w-full h-full min-h-0 min-w-0 pt-4 px-2 gap-y-6">
            <input value={appointmentFormData.details.name} onChange={(e) => setAppointmentFormData((prev) => ({ ...prev, details: { ...prev.details, name: e.target.value } }))} 
                    type="text" placeholder={t('name')} className="px-4 py-1.5 rounded-lg border border-white" />
            <input value={appointmentFormData.details.email} onChange={(e) => setAppointmentFormData((prev) => ({ ...prev, details: { ...prev.details, email: e.target.value } }))}
                    type="text" placeholder={t('email')} className="px-4 py-1.5 rounded-lg border border-white" />
            <input value={appointmentFormData.details.phoneNumber} onChange={(e) => setAppointmentFormData((prev) => ({ ...prev, details: { ...prev.details, phoneNumber: e.target.value } }))}
                    type="number" placeholder={t('phone')} className="px-4 py-1.5 rounded-lg border border-white" />
            <textarea value={appointmentFormData.details.message} onChange={(e) => setAppointmentFormData((prev) => ({ ...prev, details: { ...prev.details, message: e.target.value } }))}
                    placeholder={t('details')} name="details" id="details" className="resize-none h-1/3 px-4 py-2 rounded-lg border border-white scrollbar-hide"></textarea>
        </main>
    );
}