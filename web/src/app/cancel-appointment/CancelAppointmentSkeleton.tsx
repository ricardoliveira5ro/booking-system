import { v4 } from 'uuid';

export default function CancelAppointmentSkeleton() {

    return (
    <div className="flex flex-col gap-y-8 animate-pulse">
      <div className="border rounded-lg px-8 py-2">
        <div className="h-6 w-52 bg-gray-600 rounded"></div>
      </div>
      <div className="border rounded-lg px-2 py-1">
        {[1, 2].map((i) => (
          <div key={v4()}>
            {i > 1 && <hr className="text-gray-700" />}
            <div className="flex justify-between items-center rounded-lg gap-x-8 px-3 py-1.5">
              <div className="flex flex-col gap-y-1">
                <div className="h-5 w-24 bg-gray-600 rounded"></div>
                <div className="h-4 w-16 bg-gray-700 rounded"></div>
              </div>
              <div className="h-5 w-10 bg-gray-600 rounded"></div>
            </div>
          </div>
        ))}
      </div>
      <div className="flex flex-col gap-y-4">
        <div className="h-10 w-full bg-gray-600 rounded-lg"></div>
        <div className="h-10 w-full bg-gray-700 rounded-lg"></div>
      </div>
    </div>
  );
}