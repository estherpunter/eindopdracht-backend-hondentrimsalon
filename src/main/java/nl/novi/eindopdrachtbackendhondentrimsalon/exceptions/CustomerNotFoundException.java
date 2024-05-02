package nl.novi.eindopdrachtbackendhondentrimsalon.exceptions;

public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CustomerNotFoundException(Long customerId) {
        super("Cannot find customer with Id; " + customerId);
    }

}
