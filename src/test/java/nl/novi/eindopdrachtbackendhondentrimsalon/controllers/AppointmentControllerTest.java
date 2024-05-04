package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Appointment;
import nl.novi.eindopdrachtbackendhondentrimsalon.models.Receipt;
import nl.novi.eindopdrachtbackendhondentrimsalon.services.AppointmentService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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


    @BeforeEach
    void setUp() {
        appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setDate(LocalDateTime.now());
        appointmentDto.setCustomerId(1L);
        appointmentDto.setDogId(1L);
        appointmentDto.setStatus("Scheduled");
        appointmentDto.setProductIds(Arrays.asList(1L, 2L));
        appointmentDto.setTreatmentIds(Collections.singletonList(1L));

        scheduledAppointmentDto = new AppointmentDto();
        scheduledAppointmentDto.setId(1L);
        scheduledAppointmentDto.setDate(LocalDateTime.now());
        scheduledAppointmentDto.setCustomerId(1L);
        scheduledAppointmentDto.setDogId(1L);
        scheduledAppointmentDto.setStatus("Scheduled");
        scheduledAppointmentDto.setProductIds(Arrays.asList(1L, 2L));
        scheduledAppointmentDto.setTreatmentIds(Collections.singletonList(1L));
    }

    @Test
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
    void scheduleAppointment() throws Exception {
        // Arrange
        when(appointmentService.scheduleAppointment(any(AppointmentDto.class))).thenReturn(scheduledAppointmentDto);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments")
                .content(objectMapper.writeValueAsString(appointmentDto))
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
    void updateAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;

        // Assuming update was successful, mock the appointment service behavior
        doNothing().when(appointmentService).updateAppointment(any(AppointmentDto.class));

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/appointments/{appointmentId}", appointmentId)
                .content(objectMapper.writeValueAsString(appointmentDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
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
    void addProductToAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;
        long productId = 1L;
        doNothing().when(appointmentService).addProductToAppointment(appointmentId, productId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/products/{productId}", appointmentId, productId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addTreatmentToAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;
        long treatmentId = 1L;
        doNothing().when(appointmentService).addTreatmentToAppointment(appointmentId, treatmentId);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/treatments/{treatmentId}", appointmentId, treatmentId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void addCustomTreatmentToAppointment() throws Exception {
        // Arrange
        long appointmentId = 1L;
        double customPrice = 50.0;
        Appointment updatedAppointment = new Appointment(); // mock the returned appointment
        when(appointmentService.addCustomTreatmentToAppointment(appointmentId, customPrice)).thenReturn(updatedAppointment);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/custom-treatment", appointmentId)
                .param("customPrice", String.valueOf(customPrice))
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void generateReceipt() throws Exception {
        // Arrange
        long appointmentId = 1L;
        Receipt receipt = new Receipt(); // mock the generated receipt
        when(appointmentService.generateReceipt(appointmentId)).thenReturn(receipt);

        // Act
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/appointments/{appointmentId}/generate-receipt", appointmentId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}