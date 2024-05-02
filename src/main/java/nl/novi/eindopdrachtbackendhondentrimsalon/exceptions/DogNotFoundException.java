package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

public class DogNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DogNotFoundException(Long dogId) {
        super("Cannot find dog " + dogId);
    }
}

