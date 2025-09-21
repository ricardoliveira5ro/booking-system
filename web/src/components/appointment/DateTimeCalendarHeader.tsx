import { Calendar, MoveLeft, MoveRight } from "lucide-react";

export function DateTimeCalendarHeader({ monthLabel, onPrev, onNext }: {
    monthLabel: string;
    onPrev: () => void;
    onNext: () => void;
}) {
    return (
        <div className="flex items-center justify-between gap-x-4">
            <div className="flex gap-x-2 items-center justify-center">
                <Calendar color="var(--orange)" />
                <span>{monthLabel}</span>
            </div>
            <div className="flex gap-x-2 items-center justify-center">
                <MoveLeft onClick={onPrev} className="cursor-pointer" size={28} />
                <MoveRight onClick={onNext} className="cursor-pointer" size={28} />
            </div>
        </div>
    );
}