package nl.novi.eindopdrachtbackendhondentrimsalon.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "customers")
public class Customer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Dog> dogs;

    public Customer(){

    }
    public Customer(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public String getPhonenumber() {
        return phoneNumber;
    }

    public void setPhonenumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
