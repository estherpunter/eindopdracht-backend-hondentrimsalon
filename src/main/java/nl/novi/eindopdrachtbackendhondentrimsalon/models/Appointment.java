package nl.novi.eindopdrachtbackendhondentrimsalon.models;

import jakarta.persistence.*;
import org.apache.catalina.util.CustomObjectInputStream;

import java.util.Date;
import java.util.List;

@Entity
public class Appointment {

    @Id
    @GeneratedValue
    private Long id;
    private Date date;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private Dog dog;

    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<Treatment> treatments;

    public Appointment() {

    }

    public Appointment(Date date, Customer customer, Dog dog) {
        this.date = date;
        this.customer = customer;
        this.dog = dog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
