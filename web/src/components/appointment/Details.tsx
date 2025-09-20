import { useTranslations } from 'next-intl';
import './appointment.css'

export default function Details() {

    const t = useTranslations('appointment');

    return (
        <main className="flex flex-col w-full px-2 gap-y-4">
            <input type="text" placeholder={t('name')} className="px-4 py-1.5 rounded-lg border border-white" />
            <input type="text" placeholder={t('email')} className="px-4 py-1.5 rounded-lg border border-white" />
            <input type="text" placeholder={t('phone')} className="px-4 py-1.5 rounded-lg border border-white" />
            <textarea placeholder={t('details')} name="details" id="details" className="resize-none h-1/3 px-4 py-2 rounded-lg border border-white scrollbar-hide"></textarea>
        </main>
    );
}