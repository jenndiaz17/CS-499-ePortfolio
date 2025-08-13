import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * AppointmentService handles creating, retrieving, and deleting appointments.
 * Appointments are stored in an in-memory HashMap with the appointment ID as the key.
 */
public class AppointmentService_enhance {

    private Map<String, Appointment> appointments = new HashMap<>();

    /**
     * Creates a new appointment and adds it to the system.
     * 
     * @param appointment The Appointment object to be added
     * @throws IllegalArgumentException if the appointment is invalid
     */
    public void createAppointment(Appointment appointment) {
        validateAppointment(appointment);

        if (appointments.containsKey(appointment.getAppointmentID())) {
            throw new IllegalArgumentException("Appointment ID already exists");
        }

        appointments.put(appointment.getAppointmentID(), appointment);
        System.out.println("Appointment added: " + appointment.getAppointmentID());
    }

    /**
     * Retrieves an appointment by its ID.
     * 
     * @param appointmentID The ID of the appointment to retrieve
     * @return The corresponding Appointment object
     * @throws IllegalArgumentException if the appointment is not found
     */
    public Appointment getAppointment(String appointmentID) {
        if (appointmentID == null || !appointments.containsKey(appointmentID)) {
            throw new IllegalArgumentException("Appointment not found");
        }
        return appointments.get(appointmentID);
    }

    /**
     * Deletes an appointment from the system.
     * 
     * @param appointmentID The ID of the appointment to delete
     * @throws IllegalArgumentException if the appointment is not found
     */
    public void deleteAppointment(String appointmentID) {
        if (appointmentID == null || !appointments.containsKey(appointmentID)) {
            throw new IllegalArgumentException("Appointment not found");
        }

        appointments.remove(appointmentID);
        System.out.println("Appointment deleted: " + appointmentID);
    }

    /**
     * Validates the fields of an Appointment object.
     * 
     * @param appointment The Appointment object to validate
     * @throws IllegalArgumentException if any validation fails
     */
    private void validateAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        if (appointment.getAppointmentID() == null || appointment.getAppointmentID().isEmpty()) {
            throw new IllegalArgumentException("Appointment ID cannot be null or empty");
        }
        if (appointment.getAppointmentDate() == null || appointment.getAppointmentDate().before(new Date())) {
            throw new IllegalArgumentException("Appointment date cannot be null or in the past");
        }
        if (appointment.getDescription() == null || appointment.getDescription().length() > 50) {
            throw new IllegalArgumentException("Description cannot be null or longer than 50 characters");
        }
    }
}