export function DateTimeDaySelector({ days, onDayClick, isSelected }: {
    days: { dayNumber: number; weekday: string; date: Date }[];
    onDayClick: (day: Date) => void;
    isSelected: (day: Date) => boolean;
}) {
    return (
        <div className="w-full overflow-x-auto scrollbar-hide">
            <div className="flex gap-x-4 flex-nowrap">
                {days.map(({ dayNumber, weekday, date }) => (
                    <button key={dayNumber} onClick={() => onDayClick(date)} className="flex flex-col items-center justify-center gap-y-2">
                        <div className={`flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center cursor-pointer 
                                            ${isSelected(date) ? "bg-[var(--orange)] text-white" : "bg-white text-black"}`}>
                            {dayNumber}
                        </div>
                        <span className="text-sm">{weekday}</span>
                    </button>
                ))}
            </div>
        </div>
    );
}