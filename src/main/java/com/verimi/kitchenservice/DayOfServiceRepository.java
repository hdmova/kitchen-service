package com.verimi.kitchenservice;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface DayOfServiceRepository extends CrudRepository<DayOfService, Long> {
    DayOfService findByCalendarDay(Date calendarDay);
}
