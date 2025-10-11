import { v4 } from 'uuid';

export function ServiceSkeleton() {
    return (
        <>
            {Array.from({ length: 3 }).map(() => (
                <div key={v4()} className="animate-pulse">
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-1">
                            <div className="h-5 w-32 bg-gray-300 rounded"></div>
                            <div className="h-3 w-20 bg-gray-200 rounded"></div>
                        </div>
                        <div className="h-5 w-5 bg-gray-300 rounded"></div>
                    </div>
                    <hr className="text-gray-500" />
                </div>
            ))}
        </>
    );
}