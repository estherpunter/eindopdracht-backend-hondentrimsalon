package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

import java.io.Serial;

public class DogNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DogNotFoundException(Long dogId) {
        super("Cannot find dog " + dogId);
    }
}

