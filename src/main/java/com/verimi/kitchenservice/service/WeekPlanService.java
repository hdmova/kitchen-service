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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import org.springframework.stereotype.Service;

@Service
public class WeekPlanService {

    private static final int VALID_WEEK_RANGE = 6;
    private final StaffMemberRepository staffMemberRepository;
    private final DayOfServiceRepository dayOfServiceRepository;
    private final WeekPlanValidator weekPlanValidator;

    public WeekPlanService(StaffMemberRepository staffMemberRepository, DayOfServiceRepository dayOfServiceRepository, WeekPlanValidator weekPlanValidator) {
        this.staffMemberRepository = staffMemberRepository;
        this.dayOfServiceRepository = dayOfServiceRepository;
        this.weekPlanValidator = weekPlanValidator;
    }

    public List<DayOfService> createWeekPlan(GetWeekPlanRequestDTO weekPlanRequestDTO) {
        List<DayOfService> result = new ArrayList<>();

        if (!weekPlanValidator.isDateWithInRange(weekPlanRequestDTO.getPlanDate(), VALID_WEEK_RANGE)) {
            throw new InvalidWeekRangeException("Plan requested for unsupported week range");
        }

        Date[] daysOfCurrentWeek = getDaysOfCurrentWeek(weekPlanRequestDTO.getPlanDate());
        List<StaffMember> allStaff = staffMemberRepository.findAll();
        Collections.shuffle(allStaff);

        for (Date day : daysOfCurrentWeek) {
            DayOfService dayOfService = dayOfServiceRepository.findByCalendarDay(day);
            if (dayOfService == null) {
                dayOfService = dayOfServiceRepository.save(new DayOfService(day));
            }
            if (dayOfService.getStaffMember() == null) {
                StaffMember staffMember = findNextStaffMemberForService(day, allStaff);
                dayOfService.setStaffMember(staffMember);
                dayOfServiceRepository.save(dayOfService);
            }
            result.add(dayOfService);
        }
        return result;
    }

    protected StaffMember findNextStaffMemberForService(Date day, List<StaffMember> allStaff) {
        ListIterator<StaffMember> staffListIterator = allStaff.listIterator();

        while (staffListIterator.hasNext()) {
            StaffMember nextStaffMember = staffListIterator.next();
            List<Vacation> vacations = nextStaffMember.getVacations();
            if (!vacations.stream().anyMatch(vacation -> vacation.getVacationDate().equals(day))) {
                allStaff.remove(nextStaffMember);
                return nextStaffMember;
            }
        }
        if (allStaff.isEmpty()) {
            throw new StaffShortageException("Not enough available staff to create plan for the week");
        } else {
            throw new VacationException("All the staff members in the company are on vacation, unable to create week plan");
        }
    }

    protected Date[] getDaysOfCurrentWeek(Date refDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getFirstDayOfWeek());
        Date[] daysOfWeek = new Date[5];
        for (int i = 0; i < 5; i++) {
            daysOfWeek[i] = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }
}