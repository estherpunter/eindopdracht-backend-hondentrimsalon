package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

import java.io.Serial;

public class TreatmentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TreatmentNotFoundException(Long treatmentId) {
        super("Treatment with id " + treatmentId + " not found.");
    }
}
