package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDto {

    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private Long customerId;

    @NotNull
    private Long dogId;

    @Size(max = 50, message = "Status cannot be longer than 50 characters")
    private String status;
    private List<Long> productIds = new ArrayList<>();
    private List<Long> treatmentIds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getDogId() {
        return dogId;
    }

    public void setDogId(Long dogId) {
        this.dogId = dogId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public List<Long> getTreatmentIds() {
        return treatmentIds;
    }

    public void setTreatmentIds(List<Long> treatmentIds) {
        this.treatmentIds = treatmentIds;
    }
}

