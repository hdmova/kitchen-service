package com.verimi.kitchenservice.common.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeekPlanValidatorTest {

    private final WeekPlanValidator weekPlanValidator = new WeekPlanValidator();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void shouldReturnTrueWhenValidDateRange() throws ParseException {
        assertEquals(weekPlanValidator.isDateWithInRange(formatter.parse(LocalDate.now().plusWeeks(1).toString()), 6), true);
    }

    @Test
    void shouldReturnFalseWhenInvalidateDateRange() throws ParseException {
        assertEquals(weekPlanValidator.isDateWithInRange(formatter.parse(LocalDate.now().plusWeeks(10).toString()), 6), false);
    }
}
