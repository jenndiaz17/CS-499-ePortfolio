import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;

// IMPORTANT: Make sure this points to your enhanced version
public class AppointmentServiceEnhanceTest {

    private AppointmentService_enhance appointmentService;

    @BeforeEach
    public void setUp() {
        appointmentService = new AppointmentService_enhance();
    }

   
    @Test
    public void testAddAppointment() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A1", futureDate, "Description for A1");
        appointmentService.createAppointment(appointment);
        Appointment retrieved = appointmentService.getAppointment("A1");

        assertEquals("A1", retrieved.getAppointmentID());
        assertEquals(futureDate, retrieved.getAppointmentDate());
        assertEquals("Description for A1", retrieved.getDescription());
    }

    
    @Test
    public void testAddDuplicateAppointmentID() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A1", futureDate, "Description");
        appointmentService.createAppointment(appointment);

        assertThrows(IllegalArgumentException.class, () -> {
            Appointment duplicate = new Appointment("A1", futureDate, "Duplicate");
            appointmentService.createAppointment(duplicate);
        });
    }

    
    @Test
    public void testDeleteAppointment() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        Appointment appointment = new Appointment("A2", futureDate, "To delete");
        appointmentService.createAppointment(appointment);
        appointmentService.deleteAppointment("A2");

        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointment("A2");
        });
    }

    
    @Test
    public void testDeleteNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.deleteAppointment("DoesNotExist");
        });
    }

    
    @Test
    public void testGetNonExistentAppointment() {
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.getAppointment("MissingID");
        });
    }

   
    @Test
    public void testAddAppointmentWithPastDate() {
        Date past = new Date(System.currentTimeMillis() - 86400000);
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A3", past, "Past date");
            appointmentService.createAppointment(appointment);
        });
    }

    
    @Test
    public void testAddAppointmentWithNullID() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment(null, futureDate, "Null ID");
            appointmentService.createAppointment(appointment);
        });
    }

    
    @Test
    public void testAddAppointmentWithNullDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A4", null, "Null date");
            appointmentService.createAppointment(appointment);
        });
    }

    
    @Test
    public void testAddAppointmentWithNullDescription() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A5", futureDate, null);
            appointmentService.createAppointment(appointment);
        });
    }

    
    @Test
    public void testAddAppointmentWithLongDescription() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        String longDesc = "This description is way too long and exceeds the 50 character limit.";
        assertThrows(IllegalArgumentException.class, () -> {
            Appointment appointment = new Appointment("A6", futureDate, longDesc);
            appointmentService.createAppointment(appointment);
        });
    }
}