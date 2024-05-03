package nl.novi.eindopdrachtbackendhondentrimsalon.config;

import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.RoleMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.mappers.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan(basePackages = "nl.novi.eindopdrachtbackendhondentrimsalon.mappers") // Scan mapper package
public class AppConfig {

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    public RoleMapper roleMapper() {
        return RoleMapper.INSTANCE;
    }
}
