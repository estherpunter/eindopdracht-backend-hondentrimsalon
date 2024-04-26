package nl.novi.eindopdrachtbackendhondentrimsalon.models;

import jakarta.persistence.*;

@Entity
@Table(name = "dogs")
public class Dog {


    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String breed;
    private int age;

    @ManyToOne
    private Customer customer;

    public Dog() {

    }

    public Dog(String name, String breed, int age) {
        this.name = name;
        this.breed = breed;
        this.age = age;
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
