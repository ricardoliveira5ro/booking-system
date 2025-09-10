"use client"

export default function Service() {

    const handleCheckboxChange = (e: any) => {
        console.log(e)
    }

    return (
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
    );
}