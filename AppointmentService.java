import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppointmentService {
    // In-memory data structure to store appointments using a HashMap
    private Map<String, Appointment> appointments = new HashMap<>();

    

    // Method to add a new appointment to the system.
    public void addAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentID() == null || appointment.getAppointmentID().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be null or empty");
        }
        if (appointment.getAppointmentDate() == null || appointment.getAppointmentDate().before(new Date())) {
            throw new IllegalArgumentException("Appointment date cannot be null or in the past");
        }
        if (appointment.getDescription() == null || appointment.getDescription().length() > 50) {
            throw new IllegalArgumentException("Description cannot be null or longer than 50 characters");
        }
        if (appointments.containsKey(appointment.getAppointmentID())) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }

        // Add the appointment to the HashMap.
        appointments.put(appointment.getAppointmentID(), appointment);
    }

    // Method to retrieve an appointment by its ID.
    public Appointment getAppointment(String appointmentID) {
        if (!appointments.containsKey(appointmentID)) {
            throw new IllegalArgumentException("Appointment not found");
        }
        return appointments.get(appointmentID);
    }

    public void deleteAppointment(String appointmentID) {
        if (!appointments.containsKey(appointmentID)) {
            throw new IllegalArgumentException("Appointment not found");
        }
        // Remove the appointment from the HashMap.
        appointments.remove(appointmentID);
    }
}