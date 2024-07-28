package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.config.TestSecurityConfig;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.DogRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto customerDto;
    private CustomerRequestDto customerRequestDto;
    private DogDto dogDto;
    private DogRequestDto dogRequestDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setPhoneNumber("123456789");

        customerRequestDto = new CustomerRequestDto();
        customerRequestDto.setCustomerName("John Doe");
        customerRequestDto.setPhoneNumber("123456789");

        dogDto = new DogDto();
        dogDto.setId(1L);
        dogDto.setName("Buddy");
        dogDto.setBreed("Golden Retriever");
        dogDto.setAge(3);

        dogRequestDto = new DogRequestDto();
        dogRequestDto.setDogName("Buddy");
        dogRequestDto.setBreed("Golden Retriever");
        dogRequestDto.setAge(3);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getAllCustomers() throws Exception {
        // Arrange
        List<CustomerDto> customers = Collections.singletonList(customerDto);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getCustomerById() throws Exception {
        // Arrange
        long customerId = 1L;
        when(customerService.getCustomerById(customerId)).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/customers/{customerId}", customerId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addCustomer() throws Exception {
        // Arrange
        when(customerService.addCustomer(any(CustomerRequestDto.class))).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/customers")
                .content(objectMapper.writeValueAsString(customerRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void updateCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        when(customerService.updateCustomer(eq(customerId), any(CustomerRequestDto.class))).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/customers/{customerId}", customerId)
                .content(objectMapper.writeValueAsString(customerRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void deleteCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        doNothing().when(customerService).deleteCustomer(customerId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/customers/{customerId}", customerId));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addDogToCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        when(customerService.addDogToCustomer(eq(customerId), any(DogRequestDto.class))).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/customers/{customerId}/dogs", customerId)
                .content(objectMapper.writeValueAsString(dogRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void removeDogFromCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        doNothing().when(customerService).removeDogFromCustomer(customerId, dogId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/customers/{customerId}/dogs/{dogId}", customerId, dogId));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getDogsByCustomerId() throws Exception {
        // Arrange
        long customerId = 1L;
        List<DogDto> dogs = Collections.singletonList(dogDto);
        when(customerService.getDogsByCustomerId(customerId)).thenReturn(dogs);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/customers/{customerId}/dogs", customerId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Buddy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].breed").value("Golden Retriever"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(3));
    }
}
