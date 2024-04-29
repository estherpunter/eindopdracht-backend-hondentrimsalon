package nl.novi.eindopdrachtbackendhondentrimsalon.models;

import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Appointment {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @Column(name = "appointment_date")
    private LocalDateTime date;

    @Column(name = "status")
    private String status;

    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<Treatment> treatments;

    public Appointment() {

    }

    public Appointment(Long id, Customer customer, Dog dog, LocalDateTime date, String status, List<Product> products, List<Treatment> treatments) {
        this.id = id;
        this.customer = customer;
        this.dog = dog;
        this.date = date;
        this.status = status;
        this.products = products;
        this.treatments = treatments;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }
}
