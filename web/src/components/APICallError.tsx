import { TriangleAlert } from "lucide-react";
import { useTranslations } from "next-intl";

export default function APICallError({ retry }: { 
    retry: () => void
 }) {

    const errors = useTranslations('errors');
    
    return (
        <div className='flex flex-col w-full justify-center items-center gap-y-4'>
            <div className='flex gap-x-2'>
                <TriangleAlert color='yellow' />
                <span>{errors('apiError')}</span>
            </div>
            <button onClick={() => retry()} className='rounded-lg py-1 px-6 bg-[var(--orange)]'>{errors('apiRetryButton')}</button>
        </div>
    )
}