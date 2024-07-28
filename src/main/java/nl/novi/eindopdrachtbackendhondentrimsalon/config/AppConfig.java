package nl.novi.eindopdrachtbackendhondentrimsalon.config;

import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = "nl.novi.eindopdrachtbackendhondentrimsalon.mappers")
public class AppConfig {
    @Bean
    public AppointmentMapper appointmentMapper() {
        return AppointmentMapper.INSTANCE;
    }

    @Bean
    public CustomerMapper customerMapper() {
        return CustomerMapper.INSTANCE;
    }

    @Bean
    public DogMapper dogMapper() {
        return DogMapper.INSTANCE;
    }

    @Bean
    public ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }

    @Bean
    public RoleMapper roleMapper() {
        return RoleMapper.INSTANCE;
    }

    @Bean
    public TreatmentMapper treatmentMapper() {
        return TreatmentMapper.INSTANCE;
    }

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }
}
