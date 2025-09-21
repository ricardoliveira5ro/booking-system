import { useTranslations } from "next-intl";
import { AppointmentData } from "@/models/Appointment";

export default function AppointmentReview({ appointmentFormData, navigateBack }: {
    appointmentFormData: AppointmentData;
    navigateBack: () => void;
}) {

    const t = useTranslations('appointment');

    return (
        <>
            <main className="flex flex-col w-full h-full min-h-0 min-w-0 px-2 gap-y-5">
                <div className="flex flex-col gap-y-3">
                    {appointmentFormData.services.map((service: { code: string; name: string; price: number; slotTime: string }) => (
                        <div key={service.code} className="flex justify-between items-center border rounded-lg gap-x-8 px-3 py-1.5">
                            <div className="flex flex-col">
                                <span>{t(`services.${service.code}`)}</span>
                                <span className="text-sm text-gray-400">{service.slotTime} min</span>
                            </div>
                            <span>{service.price} €</span>
                        </div>
                    ))}
                </div>
                <div className="flex flex-col gap-y-3 px-1">
                    <div className="flex justify-end items-center gap-x-2">
                        <span className="">Total:</span>
                        <span className="text-[var(--orange)] text-lg">{appointmentFormData.services.map(s => s.price).reduce((partialSum, price) => partialSum + price, 0)}€</span>
                    </div>
                    <hr className="text-gray-500" />
                </div>
                <div className="flex flex-col px-1 gap-y-3">
                    <div className="flex flex-col gap-y-1">
                        <span className="text-sm">{`${t('when')}?`}</span>
                        <span>{`${appointmentFormData.date.getDate()} ${t(`months.${appointmentFormData.date.getMonth()}`)}, ${t(`days.${appointmentFormData.date.getDay()}`)}, ${appointmentFormData.time}`}</span>
                    </div>
                    <hr className="text-gray-500" />
                </div>
                <div className="flex flex-col gap-y-3 px-1">
                    <div className="flex flex-col gap-y-1">
                        <p><span className="text-sm">{`${t('name')}:`}</span> {appointmentFormData.details.name}</p>
                        <p><span className="text-sm">{`${t('email')}:`}</span> {appointmentFormData.details.email}</p>
                        <p><span className="text-sm">{`${t('phone')}:`}</span> {appointmentFormData.details.phoneNumber}</p>
                    </div>
                    <hr className="text-gray-500" />
                </div>
            </main>
            <footer className="flex flex-col items-center justify-between w-full px-2 gap-y-3.5">
                <button className="cursor-pointer font-sans rounded-lg px-4 py-2 bg-[var(--orange)] w-full">{t('continueButton')}</button>
                <button onClick={navigateBack} className="cursor-pointer font-sans border rounded-lg px-4 py-1 w-full">{t('backButton')}</button>
            </footer>
        </>
    );
}