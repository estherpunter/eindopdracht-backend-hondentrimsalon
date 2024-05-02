package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

public class TreatmentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TreatmentNotFoundException(Long treatmentId) {
        super("Cannot find treatment with Id:" + treatmentId);
    }

}
