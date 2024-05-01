package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AppointmentNotFoundException(Long appointmentId) {
        super("Cannot find appointment " + appointmentId);
    }
}

