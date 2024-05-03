package nl.novi.eindopdrachtbackendhondentrimsalon.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {

    public static void main(String[] args) {
        // Plain text passwords to be encoded
        String plainTextPassword1 = "admin_password";
        String plainTextPassword2 = "cashier_password";
        String plainTextPassword3 = "doggroomer_password";

        // Create BCryptPasswordEncoder instance
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Encode passwords
        String encodedPassword1 = encoder.encode(plainTextPassword1);
        String encodedPassword2 = encoder.encode(plainTextPassword2);
        String encodedPassword3 = encoder.encode(plainTextPassword3);

        // Print encoded passwords
        System.out.println("Encoded Password 1: " + encodedPassword1);
        System.out.println("Encoded Password 2: " + encodedPassword2);
        System.out.println("Encoded Password 3: " + encodedPassword3);
    }
}

