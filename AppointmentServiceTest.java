import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;


public class AppointmentServiceTest {
    private AppointmentService appointmentService;

    // Set up a new instance of AppointmentService before each test
    @BeforeEach
    public void setUp() {
        appointmentService = new AppointmentService();
    }

    // Test adding a valid appointment
    @Test
    public void testAddAppointment() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A1", futureDate, "Description for A1");
        appointmentService.addAppointment(appointment);
        Appointment retrievedAppointment = appointmentService.getAppointment("A1");
        assertEquals("A1", retrievedAppointment.getAppointmentID());
        assertEquals(futureDate, retrievedAppointment.getAppointmentDate());
        assertEquals("Description for A1", retrievedAppointment.getDescription());
    }

    // Test adding an appointment with a duplicate ID
    @Test
    public void testAddDuplicateAppointmentID() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A1", futureDate, "Description for A1");
        appointmentService.addAppointment(appointment);
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment duplicateAppointment = new Appointment("A1", futureDate, "Duplicate description");
            appointmentService.addAppointment(duplicateAppointment);
        });
    }

    // Test deleting an appointment
    @Test
    public void testDeleteAppointment() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        Appointment appointment = new Appointment("A2", futureDate, "Description for A2");
        appointmentService.addAppointment(appointment);
        appointmentService.deleteAppointment("A2");
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointment("A2");
        });
    }

    @Test
    public void testDeleteNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.deleteAppointment("NonExistentID");
        });
    }

    @Test
    public void testGetNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointment("NonExistentID");
        });
    }

    @Test
    public void testAddAppointmentWithPastDate() {
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 1 day in the past
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A3", pastDate, "Description for A3");
            appointmentService.addAppointment(appointment);
        });
    }

    @Test
    public void testAddAppointmentWithNullID() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment(null, futureDate, "Description for A4");
            appointmentService.addAppointment(appointment);
        });
    }

    @Test
    public void testAddAppointmentWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A4", null, "Description for A4");
            appointmentService.addAppointment(appointment);  // This line will not be reached if the constructor throws the exception
        });
    }

    @Test
    public void testAddAppointmentWithNullDescription() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A5", futureDate, null);
            appointmentService.addAppointment(appointment);  // This line will not be reached if the constructor throws the exception
        });
    }

    @Test
    public void testAddAppointmentWithLongDescription() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day in the future
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A6", futureDate, "This description is too long and exceeds the 50 characters limit.");
            appointmentService.addAppointment(appointment);  // This line will not be reached if the constructor throws the exception
        });
    }
}