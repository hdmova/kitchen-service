package com.verimi.kitchenservice;

import com.verimi.kitchenservice.model.GetWeekPlanRequestDTO;
import com.verimi.kitchenservice.repository.entity.DayOfService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WeekPlanIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    private final String BASE_URI = "/week-plan";

    @Test
    @Sql("classpath:/clean-up.sql")
    void shouldCreateWeekPlan() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GetWeekPlanRequestDTO testWeekPlanRequestDTO = new GetWeekPlanRequestDTO(formatter.parse(LocalDate.now().toString()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GetWeekPlanRequestDTO> requestEntity = new HttpEntity<>(testWeekPlanRequestDTO, headers);
        ResponseEntity<DayOfService[]> response = restTemplate.exchange(BASE_URI, HttpMethod.POST, requestEntity, DayOfService[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().length, 5);
    }

    @Test
    @Sql("classpath:/clean-up.sql")
    void shouldReturnBadRequestWhenInvalidWeekPlanRequested() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GetWeekPlanRequestDTO testWeekPlanRequestDTO = new GetWeekPlanRequestDTO(formatter.parse(LocalDate.now().plusWeeks(8).toString()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GetWeekPlanRequestDTO> requestEntity = new HttpEntity<>(testWeekPlanRequestDTO, headers);
        ResponseEntity response = restTemplate.exchange(BASE_URI, HttpMethod.POST, requestEntity, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
