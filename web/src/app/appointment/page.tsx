"use client"

import { X } from "lucide-react";
import CircularStepProgress from "./CircularStepProgress";
import { useTranslations } from "next-intl";

export default function Appointment() {

    const handleCheckboxChange = (e: any) => {
        console.log(e)
    }

    const t = useTranslations('appointment');

    return (
        <div className="grid grid-rows-[20px_1fr_20px] justify-items-center min-h-screen px-8 py-16 gap-y-12">
            <header className="flex items-center justify-between gap-x-8 w-full">
                <CircularStepProgress currentStep={1} totalSteps={3} size={60} strokeWidth={4.5} />
                <h1 className="font-sans text-2xl">{t('step1')}</h1>
                <X size={32} />
            </header>
            <main className="flex flex-col w-full px-2 pt-8 gap-y-8">
                <div data-aos="fade-right" data-aos-duration="900">
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-0.5">
                            <h3 className="font-sans text-lg">Hair Cut</h3>
                            <span className="text-sm text-gray-300">40 mins</span>
                        </div>
                        <input onChange={(e) => { handleCheckboxChange(e) }} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                    </div>
                    <hr className="text-gray-500" />
                </div>
                <div data-aos="fade-right" data-aos-duration="900" data-aos-delay="100">
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-0.5">
                            <h3 className="font-sans text-lg">Beard Trimming</h3>
                            <span className="text-sm text-gray-300">20 mins</span>
                        </div>
                        <input onChange={(e) => { handleCheckboxChange(e) }} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                    </div>
                    <hr className="text-gray-500" />
                </div>
                <div data-aos="fade-right" data-aos-duration="900" data-aos-delay="200">
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-0.5">
                            <h3 className="font-sans text-lg">Hair Treatment</h3>
                            <span className="text-sm text-gray-300">20 mins</span>
                        </div>
                        <input onChange={(e) => { handleCheckboxChange(e) }} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                    </div>
                    <hr className="text-gray-500" />
                </div>
            </main>
            <footer className="flex items-center justify-between w-full px-2">
                <button className="cursor-pointer font-sans border rounded-lg px-4 py-1">{t('backButton')}</button>
                <button className="cursor-pointer font-sans rounded-lg px-4 py-1 bg-[var(--orange)]">{t('continueButton')}</button>
            </footer>
        </div>
    );
}