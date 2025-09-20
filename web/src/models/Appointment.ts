type AppointmentDetails = {
    name: string;
    email: string;
    phoneNumber: string;
    message: string;
}

export type AppointmentData = {
    services: string[];
    date: Date;
    details: AppointmentDetails;
}