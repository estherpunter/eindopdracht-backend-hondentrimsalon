package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentSchedulingRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentRequestDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private AppointmentDto appointmentDto;
    private AppointmentDto scheduledAppointmentDto;
    private AppointmentSchedulingRequestDto appointmentSchedulingRequestDto;
    private AppointmentRequestDto appointmentRequestDto;

    @BeforeEach
    void setUp() {
        appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setDate(LocalDateTime.now().plusDays(1));
        appointmentDto.setCustomerId(1L);
        appointmentDto.setDogId(1L);
        appointmentDto.setStatus("Scheduled");
        appointmentDto.setProductIds(Arrays.asList(1L, 2L));
        appointmentDto.setTreatmentIds(Collections.singletonList(1L));

        scheduledAppointmentDto = new AppointmentDto();
        scheduledAppointmentDto.setId(1L);
        scheduledAppointmentDto.setDate(LocalDateTime.now().plusDays(1));
        scheduledAppointmentDto.setCustomerId(1L);
        scheduledAppointmentDto.setDogId(1L);
        scheduledAppointmentDto.setStatus("Scheduled");
        scheduledAppointmentDto.setProductIds(Arrays.asList(1L, 2L));
        scheduledAppointmentDto.setTreatmentIds(Collections.singletonList(1L));

        appointmentSchedulingRequestDto = new AppointmentSchedulingRequestDto();
        appointmentSchedulingRequestDto.setCustomerId(1L);
        appointmentSchedulingRequestDto.setDogId(1L);
        appointmentSchedulingRequestDto.setDate(LocalDateTime.now().plusDays(1));

        appointmentRequestDto = new AppointmentRequestDto();
        appointmentRequestDto.setDate(LocalDateTime.now().plusDays(1));
        appointmentRequestDto.setStatus("Scheduled");
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getAllAppointments() throws Exception {
        // Arrange
        when(appointmentService.getAllAppointments()).thenReturn(Collections.singletonList(scheduledAppointmentDto));

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/appointments/allappointments")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productIds[0]").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].treatmentIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void getAppointmentById() throws Exception {
        // Arrange
        long appointmentId = 1L;
        when(appointmentService.getAppointmentById(appointmentId)).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/appointments/{appointmentId}", appointmentId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds[0]").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.treatmentIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void scheduleAppointment() throws Exception {
        // Arrange
        when(appointmentService.scheduleAppointment(any(AppointmentSchedulingRequestDto.class))).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments")
                .content(objectMapper.writeValueAsString(appointmentSchedulingRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds[0]").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.treatmentIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void updateAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;

        // Assuming update was successful, mock the appointment service behavior
        when(appointmentService.updateAppointment(eq(appointmentId), any(AppointmentRequestDto.class))).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/appointments/{appointmentId}", appointmentId)
                .content(objectMapper.writeValueAsString(appointmentRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds[0]").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.treatmentIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void cancelAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/appointments/{appointmentId}", appointmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addProductToAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;
        long productId = 1L;
        when(appointmentService.addProductToAppointment(appointmentId, productId)).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/products/{productId}", appointmentId, productId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void addTreatmentToAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;
        long treatmentId = 1L;
        when(appointmentService.addTreatmentToAppointment(appointmentId, treatmentId)).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/treatments/{treatmentId}", appointmentId, treatmentId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Scheduled"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.treatmentIds[0]").value(1L));
    }

    @Test
    @WithMockUser (username = "admin", roles = {"ADMIN", "DOGGROOMER", "CASHIER"})
    void generateReceipt() throws Exception {
        // Arrange
        long appointmentId = 1L;
        Receipt receipt = new Receipt();
        receipt.setCustomerId(1L);
        receipt.setCustomerName("John Doe");
        receipt.setDogId(1L);
        receipt.setDogName("Rex");
        receipt.setStatus("Completed");
        receipt.setTotalPrice(100.0);
        when(appointmentService.generateReceipt(appointmentId)).thenReturn(receipt);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/generate-receipt", appointmentId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dogId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dogName").value("Rex"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Completed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(100.0));
    }
}
