package nl.novi.eindopdrachtbackendhondentrimsalon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eindopdrachtbackendhondentrimsalon.dto.AppointmentDto;
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
                .get("/api/appointments/allAppointments")
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
    void updateAppointment() {
    }

    @Test
    void cancelAppointment() {
    }

    @Test
    void addProductToAppointment() {
    }

    @Test
    void addTreatmentToAppointment() {
    }

    @Test
    void addCustomTreatmentToAppointment() {
    }

    @Test
    void generateReceipt() {
    }
}