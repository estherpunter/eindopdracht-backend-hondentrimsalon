package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

import java.io.Serial;

public class TreatmentNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TreatmentNotFoundException(Long treatmentId) {
        super("Cannot find treatment with Id:" + treatmentId);
    }

}
