package com.verimi.kitchenservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.verimi.kitchenservice.model.GetWeekPlanRequestDTO;
import com.verimi.kitchenservice.repository.entity.DayOfService;
import com.verimi.kitchenservice.repository.entity.StaffMember;
import com.verimi.kitchenservice.service.WeekPlanService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(WeekPlanController.class)
@AutoConfigureMockMvc
class WeekPlanControllerTest {

    private static final String BASE_URL = "/week-plan";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private WeekPlanService weekPlanService;

    @Test
    public void createWeekPlanSuccessfully() throws Exception {
        GetWeekPlanRequestDTO testRequestDto = new GetWeekPlanRequestDTO(new Date());
        List<DayOfService> response = Arrays.asList(new DayOfService(1L, new Date(), new StaffMember(2L, "jane", "doe", null, null)));
        when(weekPlanService.createWeekPlan(any())).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).content(writeRequestAsJsonString(testRequestDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].staffMember.id").value(2L));
    }

    private byte[] writeRequestAsJsonString(GetWeekPlanRequestDTO requestDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(requestDTO).getBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}