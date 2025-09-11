import { Calendar, MoveLeft, MoveRight } from "lucide-react";
import './dateTime.css'

export default function DateTime() {
    
    return (
        <main className="flex flex-col w-full min-w-0 pt-4 px-2 gap-y-6">
            <div className="flex items-center justify-between gap-x-4">
                <div className="flex gap-x-2 items-center justify-center">
                    <Calendar color="var(--orange)" />
                    <span>Setembro 2025</span>
                </div>
                <div className="flex gap-x-2 items-center justify-center">
                    <MoveLeft size={28} />
                    <MoveRight size={28} />
                </div>
            </div>
            <div className="w-full overflow-x-auto scrollbar-hide">
                <div className="flex gap-x-3 flex-nowrap">
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">11</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-[var(--orange)] text-white">12</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">13</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">14</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">15</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">16</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">17</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">18</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">19</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">20</div>
                    <div className="flex-shrink-0 w-[35px] h-[35px] leading-[35px] rounded-full text-center bg-white text-black">21</div>
                </div>
            </div>
        </main>
    );
}