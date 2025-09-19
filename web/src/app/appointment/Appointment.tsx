"use client"

import { X } from "lucide-react";
import { useTranslations } from "next-intl";
import Link from "next/link";

import CircularStepProgress from "./CircularStepProgress";
import Service from "./Service";
import DateTime from "./DateTime";
import Details from "./Details";
import { useAppointmentSteps } from "@/hooks/useAppointmentSteps";
import { useState } from "react";

type AppointmentDetails = {
    name: string;
    email: string;
    phoneNumber: string;
    message: string;
}

export type AppointmentData = {
    services: string[];
    date: Date;
    details: AppointmentDetails;
}

export default function Appointment() {

    const t = useTranslations('appointment');
    const { step, back, next } = useAppointmentSteps();

    function roundUpToNext30Min(): Date {
        const rounded = new Date();
        rounded.setMinutes(Math.ceil(rounded.getMinutes() / 30) * 30, 0, 0);
        return rounded;
    }

    const [appointmentFormData, setAppointmentFormData] = useState<AppointmentData>({
        services: [],
        date: roundUpToNext30Min(),
        details: {
            name: "",
            email: "",
            phoneNumber: "",
            message: "",
        },
    });

    return (
        <div className="grid grid-rows-[auto_1fr_auto] justify-items-center h-screen px-8 py-16 gap-y-12">
            <header className="flex items-center justify-between w-full">
                <CircularStepProgress currentStep={step} totalSteps={3} size={60} strokeWidth={4.5} />
                <h1 className="font-sans text-2xl text-center">{t(`step${step}`)}</h1>
                <Link href={'/'}><X className="cursor-pointer" size={32} /></Link>
            </header>
            {step === 1 ? <Service appointmentFormData={appointmentFormData} setAppointmentFormData={setAppointmentFormData} /> :
              step === 2 ? <DateTime appointmentFormData={appointmentFormData} setAppointmentFormData={setAppointmentFormData} /> :
                            <Details />
            }
            <footer className="flex items-center justify-between w-full px-2">
                <button onClick={back} className="cursor-pointer font-sans border rounded-lg px-4 py-1">{t('backButton')}</button>
                <button onClick={next} className="cursor-pointer font-sans rounded-lg px-4 py-1 bg-[var(--orange)]">{t('continueButton')}</button>
            </footer>
        </div>
    );
}