package com.booking.system.appointment.service;

import com.booking.system.appointment.dto.*;
import com.booking.system.appointment.repository.AppointmentRepository;
import com.booking.system.common.exception.AlreadyBookingException;
import com.booking.system.common.exception.AppointmentNotFoundException;
import com.booking.system.database.entity.AppointmentEntity;
import com.booking.system.database.entity.ServiceEntity;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BusinessHoursService businessHoursService;

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${resend.api-token}")
    private String resendApiToken;

    private final ConcurrentHashMap<LocalDate, Object> locks = new ConcurrentHashMap<>();

    public AppointmentDTO getAppointment(String appointmentId) {
        AppointmentEntity appointmentEntity = appointmentRepository.findById(UUID.fromString(appointmentId))
                                                    .orElseThrow(() -> new AppointmentNotFoundException("Appointment does not exist or already cancelled"));

        return modelMapper.map(appointmentEntity, AppointmentDTO.class);
    }

    @Transactional
    public AppointmentDTO createAppointment(AppointmentRequestDTO appointmentRequest) throws IOException, ResendException {
        LocalDate appointmentDate = appointmentRequest.getAppointmentDate();
        Object lock = locks.computeIfAbsent(appointmentDate, d -> new Object());

        synchronized (lock) {
            List<ServiceEntity> services = serviceService.getServicesByCode(appointmentRequest.getServices())
                    .stream().map(s -> this.modelMapper.map(s, ServiceEntity.class))
                    .toList();

            int duration = services.stream().mapToInt(ServiceEntity::getSlotTime).sum();

            LocalTime start = appointmentRequest.getAppointmentTime();
            LocalTime end = appointmentRequest.getAppointmentTime().plusMinutes(duration);

            if (doesOverlapTimeSlot(start, end, appointmentDate))
                throw new AlreadyBookingException("Appointment already booked");

            AppointmentEntity appointment = modelMapper.map(appointmentRequest, AppointmentEntity.class);
            appointment.setServices(new HashSet<>(services));

            AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

            String cancelKey = UUID.randomUUID().toString();
            savedAppointment.setCancelKey(DigestUtils.sha256Hex(cancelKey));

            AppointmentDTO appointmentDTO = modelMapper.map(savedAppointment, AppointmentDTO.class);
            String eventId = googleCalendarService.createCalendarEvent(appointmentDTO, duration);

            savedAppointment.setCalendarEventId(eventId);
            savedAppointment = appointmentRepository.save(savedAppointment);

            sendConfirmationEmail(appointmentDTO);

            return modelMapper.map(savedAppointment, AppointmentDTO.class);
        }
    }

    @Transactional
    public void cancelAppointment(String appointmentId, String cancelKey) throws IOException {
        AppointmentEntity appointment = appointmentRepository.findById(UUID.fromString(appointmentId))
                                            .orElseThrow(() -> new AppointmentNotFoundException("Appointment does not exist or already cancelled"));

        String hashedKey = DigestUtils.sha256Hex(cancelKey);
        if (!hashedKey.equals(appointment.getCancelKey()))
            throw new AppointmentNotFoundException("Invalid cancel key");

        String eventId = appointment.getCalendarEventId();

        appointmentRepository.delete(appointment);
        googleCalendarService.deleteCalendarEvent(eventId);
    }

    public List<LocalTime> getAvailableTimeSlots(TimeSlotsRequestDTO timeSlotsRequestDTO) {
        LocalDate date = timeSlotsRequestDTO.getAppointmentDate();

        int durationRequested = serviceService.getServicesByCode(timeSlotsRequestDTO.getServices())
                                                .stream().mapToInt(ServiceDTO::getSlotTime).sum();

        BusinessHoursDTO businessHours = businessHoursService.getBusinessHoursByDay(date);
        if (businessHours.isClosed())
            return List.of();

        List<LocalTime> availableTimeSlots = new ArrayList<>();
        for (LocalTime timeSlot : generateSlots(businessHours.getStartTime(), businessHours.getEndTime())) {
            if (timeSlot.plusMinutes(durationRequested).isAfter(businessHours.getEndTime()))
                break;

            boolean isPastSlot = date.isEqual(LocalDate.now()) && timeSlot.isBefore(LocalTime.now());
            if (!isPastSlot && !doesOverlapTimeSlot(timeSlot, timeSlot.plusMinutes(durationRequested), date))
                availableTimeSlots.add(timeSlot);
        }

        return availableTimeSlots;
    }

    private List<LocalTime> generateSlots(LocalTime startTime, LocalTime endTime) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime time = startTime;

        while (!time.isAfter(endTime.minusMinutes(30))) {
            slots.add(time);
            time = time.plusMinutes(30);
        }

        return slots;
    }

    public boolean doesOverlapTimeSlot(LocalTime requestedStartTime, LocalTime requestedEndTime, LocalDate date) {
        List<AppointmentEntity> existingAppointments = appointmentRepository.findByStartAtBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX));

        for (AppointmentEntity existingAppointment : existingAppointments) {
            int duration = existingAppointment.getServices().stream().mapToInt(ServiceEntity::getSlotTime).sum();
            LocalDateTime existingAppointmentEndTime = existingAppointment.getStartAt().plusMinutes(duration);

            if (requestedStartTime.isBefore(existingAppointmentEndTime.toLocalTime()) && requestedEndTime.isAfter(existingAppointment.getStartAt().toLocalTime()))
                return true;
        }

        return false;
    }

    private void sendConfirmationEmail(AppointmentDTO appointmentDTO) throws ResendException {
        Map<String, Object> vars = Map.of(
                "name", "User",
                "date", LocalDate.now().toString(),
                "time", LocalTime.now().toString(),
                "services", "Hair Cut",
                "cancelLink", "http://localhost:3000/cancel-appointment"
        );

        String htmlBody = emailTemplateService.buildAppointmentEmail(vars);

        Resend resend = new Resend(resendApiToken);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Barber Booking <barberbooking@resend.dev>")
                .to(appointmentDTO.getDetails().getEmail())
                .subject("")
                .html(htmlBody)
                .build();

        resend.emails().send(params);
    }
}
