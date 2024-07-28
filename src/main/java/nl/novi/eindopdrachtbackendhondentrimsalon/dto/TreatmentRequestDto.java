package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TreatmentRequestDto {

    @NotBlank
    @Size(min = 1, max = 100)
    private String treatmentName;

    @NotNull
    private double price;

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
