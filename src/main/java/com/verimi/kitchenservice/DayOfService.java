package com.verimi.kitchenservice;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DayOfService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date calendarDay;

    @ManyToOne
    private StaffMember staffMember;

    public DayOfService() {
    }

    public DayOfService(Date day) {
        this.calendarDay = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(Date calendarDay) {
        this.calendarDay = calendarDay;
    }

    public StaffMember getStaffMember() {
        return staffMember;
    }

    public void setStaffMember(StaffMember staffMember) {
        this.staffMember = staffMember;
    }
}
