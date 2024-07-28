package nl.novi.eindopdrachtbackendhondentrimsalon.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DogRequestDto {

    @NotBlank
    @Size(min = 1, max = 100)
    private String dogName;

    @NotBlank
    @Size(min = 1, max = 100)
    private String breed;

    @Min(0)
    private int age;

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
