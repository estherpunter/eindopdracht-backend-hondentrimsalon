package nl.novi.eindopdrachtbackendhondentrimsalon.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "customers")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phonenumber;

    @OneToMany(mappedBy = "customer")
    private List<Dog> dogs;

    public Customer(){

    }
    public Customer(String name, String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
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
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
