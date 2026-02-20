package com.verimi.kitchenservice;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class WeekPlanControllerTest {
    @Mock StaffMemberRepository staffMemberRepository;
    @Mock DayOfServiceRepository dayOfServiceRepository;
    private final WeekPlanController sut = new WeekPlanController(staffMemberRepository, dayOfServiceRepository);

    @Test
    void checkWeekContent() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = sut.getDaysOfCurrentWeek(sdf.parse("2020-12-14"));
        assertEquals("2020-12-14", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2020-12-15", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2020-12-16", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2020-12-17", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2020-12-18", sdf.format(daysOfCurrentWeek[4]));
    }

    @Test
    void checkWeekEndOfYearContent() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = sut.getDaysOfCurrentWeek(sdf.parse("2020-01-01"));
        assertEquals("2019-12-30", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2019-12-31", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2020-01-01", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2020-01-02", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2020-01-03", sdf.format(daysOfCurrentWeek[4]));
    }
    @Test
    void checkWeekInFuture() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = sut.getDaysOfCurrentWeek(sdf.parse("2025-02-16"));
        assertEquals("2025-02-10", sdf.format(daysOfCurrentWeek[0]));
        assertEquals("2025-02-11", sdf.format(daysOfCurrentWeek[1]));
        assertEquals("2025-02-12", sdf.format(daysOfCurrentWeek[2]));
        assertEquals("2025-02-13", sdf.format(daysOfCurrentWeek[3]));
        assertEquals("2025-02-14", sdf.format(daysOfCurrentWeek[4]));
    }

    @Test
    void checkSize() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date[] daysOfCurrentWeek = sut.getDaysOfCurrentWeek(sdf.parse("2020-01-01"));
        assertEquals(5, daysOfCurrentWeek.length);

    }

}