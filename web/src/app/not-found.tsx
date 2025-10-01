import { MoveLeft } from "lucide-react";
import Link from "next/link";

export default function NotFound() {

    return (
        <div className="flex flex-col w-full h-screen justify-center items-center px-8 py-16 gap-y-4">
            <h1 className="text-8xl">404</h1>
            <span className="text-lg">Page Not Found</span>
            <Link href={'/'} className="flex w-fit rounded-lg gap-x-2 px-8 py-1 bg-[var(--orange)] justify-center items-center">
                <MoveLeft />
                <span className="text-lg">Go Back</span>
            </Link>
        </div>
    );
}