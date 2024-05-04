package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.CustomerDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto customerDto;


    @BeforeEach
    void setUp() {
        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
        customerDto.setPhoneNumber("123456789");
    }

    @Test
    void getAllCustomers() throws Exception {
        // Arrange
        List<CustomerDto> customers = Arrays.asList(customerDto);
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
    void addCustomer() throws Exception {
        // Arrange
        when(customerService.addCustomer(anyString(), anyString())).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/customers")
                .param("customerName", "John Doe")
                .param("phoneNumber", "123456789")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    void updateCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        when(customerService.updateCustomer(eq(customerId), any(CustomerDto.class))).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/customers/{customerId}", customerId)
                .content(objectMapper.writeValueAsString(customerDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    void deleteCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        doNothing().when(customerService).deleteCustomer(customerId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/customers/{customerId}", customerId));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void addDogToCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        String dogName = "Buddy";
        String breed = "Golden Retriever";
        int age = 3;

        when(customerService.addDogToCustomer(eq(customerId), eq(dogName), eq(breed), eq(age))).thenReturn(customerDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/customers/{customerId}/dogs", customerId)
                .param("dogName", dogName)
                .param("breed", breed)
                .param("age", String.valueOf(age))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("123456789"));
    }

    @Test
    void updateDogForCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        String dogName = "Max";
        String breed = "Labrador";
        int age = 5;

        doNothing().when(customerService).updateDogForCustomer(eq(customerId), eq(dogId), eq(dogName), eq(breed), eq(age));

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/customers/{customerId}/dogs/{dogId}", customerId, dogId)
                .param("dogName", dogName)
                .param("breed", breed)
                .param("age", String.valueOf(age))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void removeDogFromCustomer() throws Exception {
        // Arrange
        long customerId = 1L;
        long dogId = 1L;
        doNothing().when(customerService).removeDogFromCustomer(customerId, dogId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/customers/{customerId}/dogs/{dogId}", customerId, dogId));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}