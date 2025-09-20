"use client"

import { X } from "lucide-react";
import { useState } from "react";
import { useTranslations } from "next-intl";
import Link from "next/link";

import CircularStepProgress from "../../components/appointment/CircularStepProgress";
import Service from "../../components/appointment/Service";
import DateTime from "../../components/appointment/DateTime";
import Details from "../../components/appointment/Details";

import { useAppointmentSteps } from "@/hooks/useAppointmentSteps";

import { roundUpToNext30Min } from "@/utils/functions";
import { AppointmentData } from "@/models/Appointment";

export default function Appointment() {

    const t = useTranslations('appointment');
    const { step, back, next } = useAppointmentSteps();

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