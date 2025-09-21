type AppointmentDetails = {
    name: string;
    email: string;
    phoneNumber: string;
    message: string;
}

export type AppointmentData = {
    services: string[];
    date: Date;
    time: string | null;
    details: AppointmentDetails;
}