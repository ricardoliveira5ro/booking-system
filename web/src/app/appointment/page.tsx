"use client"

import { X } from "lucide-react";
import { useTranslations } from "next-intl";

import CircularStepProgress from "./CircularStepProgress";
import Service from "./Service";
import { useState } from "react";
import Link from "next/link";
import DateTime from "./DateTime";
import Details from "./Details";
import { useRouter } from "next/navigation";

export default function Appointment() {

    const t = useTranslations('appointment');
    const router = useRouter()

    const [step, setStep] = useState(1);

    const handleStep = (direction: string) => {
        if (direction === 'NEXT') {
            if (step + 1 > 3) {
                // Handle confirmation
                router.replace('/');
            } else {
                setStep((prev) => prev + 1);
            }
            return;
        }

        if (direction === 'BACK') {
            if (step - 1 < 1) {
                router.replace('/');
            } else {
                setStep((prev) => Math.max(prev - 1, 1));
            }
        }
    }

    return (
        <div className="grid grid-rows-[auto_1fr_auto] justify-items-center h-screen px-8 py-16 gap-y-12">
            <header className="flex items-center justify-between w-full">
                <CircularStepProgress currentStep={step} totalSteps={3} size={60} strokeWidth={4.5} />
                <h1 className="font-sans text-2xl text-center">{t(`step${step}`)}</h1>
                <Link href={'/'}><X className="cursor-pointer" size={32} /></Link>
            </header>
            {step === 1 ? <Service /> :
              step === 2 ? <DateTime /> :
                            <Details />
            }
            <footer className="flex items-center justify-between w-full px-2">
                <button onClick={() => handleStep("BACK")} className="cursor-pointer font-sans border rounded-lg px-4 py-1">{t('backButton')}</button>
                <button onClick={() => handleStep("NEXT")} className="cursor-pointer font-sans rounded-lg px-4 py-1 bg-[var(--orange)]">{t('continueButton')}</button>
            </footer>
        </div>
    );
}