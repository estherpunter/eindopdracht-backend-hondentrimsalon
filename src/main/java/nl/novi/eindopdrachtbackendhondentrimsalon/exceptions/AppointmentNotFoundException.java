package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

import java.io.Serial;

public class AppointmentNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AppointmentNotFoundException(Long appointmentId) {
        super("Cannot find appointment " + appointmentId);
    }

}

