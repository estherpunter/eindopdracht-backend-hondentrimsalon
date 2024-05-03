package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

import java.io.Serial;

public class ProductNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(Long productId) {
        super("Cannot find product " + productId);
    }

}


