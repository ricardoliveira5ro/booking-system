import { useState } from "react";
import { useRouter } from "next/navigation";

export function useAppointmentSteps() {
    const [step, setStep] = useState(1);
    const router = useRouter();

    const next = () => {
        if (step + 1 > 4)
            router.replace("/");
        else 
            setStep((prev) => prev + 1);
    };

    const back = () => {
        if (step - 1 < 1) 
            router.replace("/");
        else 
            setStep((prev) => prev - 1);
    };

    return { step, next, back };
}