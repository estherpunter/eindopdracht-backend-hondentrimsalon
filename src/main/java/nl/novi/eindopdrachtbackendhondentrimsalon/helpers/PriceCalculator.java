package nl.novi.eindopdrachtbackendhondentrimsalon.helpers;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Product;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Treatment;

import java.util.List;

public class PriceCalculator {

    public static double calculateTotalPrice(List<Product> products, List<Treatment> treatments) {
        double totalPrice = 0.0;

        for (Product product : products) {
            totalPrice += product.getPrice();
        }

        for (Treatment treatment : treatments) {
            totalPrice += treatment.getPrice();
        }
        return totalPrice;
    }

}
