package com.verimi.kitchenservice.service;

import com.verimi.kitchenservice.common.error.exceptions.InvalidWeekRangeException;
import com.verimi.kitchenservice.common.error.exceptions.StaffShortageException;
import com.verimi.kitchenservice.common.error.exceptions.VacationException;
import com.verimi.kitchenservice.common.validator.WeekPlanValidator;
import com.verimi.kitchenservice.model.GetWeekPlanRequestDTO;
import com.verimi.kitchenservice.repository.DayOfServiceRepository;
import com.verimi.kitchenservice.repository.StaffMemberRepository;
import com.verimi.kitchenservice.repository.entity.DayOfService;
import com.verimi.kitchenservice.repository.entity.StaffMember;
import com.verimi.kitchenservice.repository.entity.Vacation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeekPlanServiceTest {

    private final StaffMemberRepository staffMemberRepository = mock(StaffMemberRepository.class);
    private final DayOfServiceRepository dayOfServiceRepository = mock(DayOfServiceRepository.class);
    private final WeekPlanValidator weekPlanValidator = mock(WeekPlanValidator.class);
    private final WeekPlanService weekPlanService = new WeekPlanService(staffMemberRepository, dayOfServiceRepository, weekPlanValidator);

    @Test
    void shouldReturnNextStaffMemberForKitchenServiceSuccessfully() {
        List<StaffMember> staffMemberList = new ArrayList<>();
        staffMemberList.add(new StaffMember(1L, "Jane", "Doe", null, Collections.emptyList()));

        StaffMember staffMember = weekPlanService.findNextStaffMemberForService(new Date(), staffMemberList);

        assertEquals(staffMember.getId(), 1L);
        assertEquals(staffMember.getFirstName(), "Jane");
        assertEquals(staffMember.getLastName(), "Doe");
    }

    @Test
    void shouldThrowStaffShortageException() {
        assertThrows(StaffShortageException.class, () -> weekPlanService.findNextStaffMemberForService(new Date(), Collections.emptyList()));
    }

    @Test
    void shouldThrowStaffOnVacationException() {
        List<StaffMember> staffMemberList = new ArrayList<>();
        Long currentTime = System.currentTimeMillis();
        staffMemberList.add(new StaffMember(1L, "Jane", "Doe", null, Arrays.asList(new Vacation(1l, new Date(currentTime)))));

        assertThrows(VacationException.class, () -> weekPlanService.findNextStaffMemberForService(new Date(currentTime), staffMemberList));
    }

    @Test
    void shouldSuccessfullyCreateWeekPlan() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GetWeekPlanRequestDTO testWeekPlanRequestDTO = new GetWeekPlanRequestDTO(formatter.parse(LocalDate.now().toString()));
        List<StaffMember> staffMemberList = new ArrayList<>();
        staffMemberList.add(new StaffMember(1L, "Jane", "Doe", null, Collections.emptyList()));
        staffMemberList.add(new StaffMember(2L, "adam", "mckay", null, Collections.emptyList()));
        staffMemberList.add(new StaffMember(3L, "mary", "allen", null, Collections.emptyList()));
        staffMemberList.add(new StaffMember(4L, "chris", "hem", null, Collections.emptyList()));
        staffMemberList.add(new StaffMember(5L, "ross", "geller", null, Collections.emptyList()));
        List<StaffMember> resultStaffMembers = new ArrayList<>(staffMemberList);

        when(weekPlanValidator.isDateWithInRange(testWeekPlanRequestDTO.getPlanDate(), 6)).thenReturn(true);
        when(staffMemberRepository.findAll()).thenReturn(staffMemberList);
        when(dayOfServiceRepository.findByCalendarDay(any())).thenReturn(null);
        when(dayOfServiceRepository.save(any())).then(i -> i.getArguments()[0]);

        List<DayOfService> dayOfServiceList = weekPlanService.createWeekPlan(testWeekPlanRequestDTO);

        assertEquals(dayOfServiceList.isEmpty(), false);
        assertEquals(staffMemberList.size(), 0);
        dayOfServiceList.stream().forEach(
                it -> assertEquals(resultStaffMembers.contains(it.getStaffMember()), true)
        );
    }

    @Test
    void shouldThrowInvalidWeekRangeException() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GetWeekPlanRequestDTO testWeekPlanRequestDTO = new GetWeekPlanRequestDTO(formatter.parse(LocalDate.now().plusWeeks(10).toString()));

        assertThrows(InvalidWeekRangeException.class, () -> weekPlanService.createWeekPlan(testWeekPlanRequestDTO));
    }

    @Test
    void checkWeekContent() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = weekPlanService.getDaysOfCurrentWeek(sdf.parse("2020-12-14"));
        assertEquals("2020-12-14", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2020-12-15", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2020-12-16", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2020-12-17", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2020-12-18", sdf.format(daysOfCurrentWeek[4]));
    }

    @Test
    void checkWeekEndOfYearContent() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = weekPlanService.getDaysOfCurrentWeek(sdf.parse("2020-01-01"));
        assertEquals("2019-12-30", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2019-12-31", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2020-01-01", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2020-01-02", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2020-01-03", sdf.format(daysOfCurrentWeek[4]));
    }

    @Test
    void checkWeekInFuture() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = weekPlanService.getDaysOfCurrentWeek(sdf.parse("2025-02-16"));
        assertEquals("2025-02-10", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2025-02-11", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2025-02-12", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2025-02-13", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2025-02-14", sdf.format(daysOfCurrentWeek[4]));
    }

    @Test
    void checkSize() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = weekPlanService.getDaysOfCurrentWeek(sdf.parse("2020-01-01"));
        assertEquals(5, daysOfCurrentWeek.length);
    }
}
