package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(Long productId) {
        super("Cannot find product " + productId);
    }
}


