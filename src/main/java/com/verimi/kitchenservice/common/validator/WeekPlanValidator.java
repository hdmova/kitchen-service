package com.verimi.kitchenservice.common.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class WeekPlanValidator {

    public boolean isDateWithInRange(Date inputDate, int range) {
        LocalDate inputLocalDate = LocalDate.from(inputDate.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        if (inputLocalDate.isBefore(LocalDate.now().minusWeeks(range)) || inputLocalDate.isAfter(LocalDate.now().plusWeeks(range))) {
            return false;
        }
        return true;
    }
}
