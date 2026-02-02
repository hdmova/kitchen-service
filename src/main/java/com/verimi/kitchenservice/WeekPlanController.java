package com.verimi.kitchenservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class WeekPlanController {

    private static final Logger logger = LoggerFactory.getLogger(StaffMemberController.class);
    private final StaffMemberRepository staffMemberRepository;
    private final DayOfServiceRepository dayOfServiceRepository;

    public WeekPlanController(StaffMemberRepository staffMemberRepository, DayOfServiceRepository dayOfServiceRepository) {
        this.staffMemberRepository = staffMemberRepository;
        this.dayOfServiceRepository = dayOfServiceRepository;
    }

    @GetMapping("/current_week")
    List<DayOfService> all() {
        List<DayOfService> result = new ArrayList<DayOfService>();

        Date[] daysOfCurrentWeek = getDaysOfCurrentWeek(new Date());
        for (Date day: daysOfCurrentWeek) {
            DayOfService dayOfService = dayOfServiceRepository.findByCalendarDay(day);
            if (dayOfService == null) {
                dayOfService = dayOfServiceRepository.save(new DayOfService(day));
            }
            if(dayOfService.getStaffMember() == null) {
                StaffMember staffMember = findNextStaffMemberForService();
                dayOfService.setStaffMember(staffMember);
                staffMember.setLastService(dayOfService.getCalendarDay());
                staffMemberRepository.save(staffMember);
            }
            dayOfServiceRepository.save(dayOfService);
            result.add(dayOfService);
        }

        return result;
    }

    protected StaffMember findNextStaffMemberForService() {
        List<StaffMember> all = staffMemberRepository.findAll();

        all.sort(new Comparator<StaffMember>() {
            @Override
            public int compare(StaffMember o1, StaffMember o2) {
                if(o1.getLastService() == null) {
                    return -1;
                }
                if(o2.getLastService() == null) {
                    return 1;
                }
                return o1.getLastService().compareTo(o2.getLastService());
            }
        });

        return all.get(0);
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
