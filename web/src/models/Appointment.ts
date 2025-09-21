type AppointmentService = {
    code: string;
    name: string;
    price: number
    slotTime: string;
}

type AppointmentDetails = {
    name: string;
    email: string;
    phoneNumber: string;
    message: string;
}

export type AppointmentData = {
    services: AppointmentService[];
    date: Date;
    time: string | null;
    details: AppointmentDetails;
}