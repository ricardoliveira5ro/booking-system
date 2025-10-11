import { v4 } from 'uuid';

export function DateTimeSlotSkeleton() {
    return (
        <>
        {Array.from({ length: 7 }).map(() => (
            <div key={v4()} className="flex flex-col gap-y-4 animate-pulse">
                <div className="flex justify-between items-center">
                    <div className="h-5 w-16 bg-gray-300 rounded"></div>
                    <div className="h-5 w-5 bg-gray-300 rounded-full"></div>
                </div>
                <hr />
            </div>
        ))}
        </>
    );
}