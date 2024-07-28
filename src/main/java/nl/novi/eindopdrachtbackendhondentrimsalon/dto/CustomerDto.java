package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CustomerDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 10, max = 20)
    private String phoneNumber;

    private List<Long> dogIds;

    public CustomerDto() {
    }

    public CustomerDto(Long id, String name, String phoneNumber, List<Long> dogIds) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.dogIds = dogIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Long> getDogIds() {
        return dogIds;
    }

    public void setDogIds(List<Long> dogIds) {
        this.dogIds = dogIds;
    }
}
