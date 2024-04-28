package nl.novi.eindopdrachtbackendhondentrimsalon.repository;

import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Customer;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    //Find appointments by Customer
    List<Appointment> findByCustomer(Customer customer);

    //Find appointments by Date
    List<Appointment> findByDate(Date date);

    //Find appointments by Dog
    List<Appointment> findByDog(Dog dog);
}
