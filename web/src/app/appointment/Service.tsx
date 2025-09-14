"use client"

import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function Service() {

    const [selectedServices, setSelectedServices] = useState<string[]>([])

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>, code: string) => {
        e.target.checked ? setSelectedServices((prev) => [...prev, code]) :
                            setSelectedServices((prev) => prev.filter((c) => c !== code))
    }

    const { data, isPending, error } = useQuery({
        queryKey: ['services'],
        queryFn: () => fetch('http://localhost:8081/api/appointment/services').then(r => r.json()),
    })

    return (
        <main className="flex flex-col w-full px-2 pt-8 gap-y-8">
            {!isPending && data.map((s: any, index: number) => (
                <div key={s.code} data-aos="fade-right" data-aos-duration="900" data-aos-delay={`${index * 100}`}>
                    <div className="flex pb-4 gap-x-4">
                        <div className="flex flex-col w-full gap-y-0.5">
                            <h3 className="font-sans text-lg">{s.name}</h3>
                            <span className="text-sm text-gray-300">{s.slotTime} mins</span>
                        </div>
                        <input checked={selectedServices.includes(s.code)} onChange={(e) => { handleCheckboxChange(e, s.code) }} type="checkbox" className="accent-[var(--orange)] cursor-pointer"></input>
                    </div>
                    <hr className="text-gray-500" />
                </div>
            ))}
        </main>
    );
}