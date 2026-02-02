package com.verimi.kitchenservice.controller;

import com.verimi.kitchenservice.common.error.exceptions.StaffMemberNotFoundException;
import com.verimi.kitchenservice.model.CreateStaffMemberRequestDTO;
import com.verimi.kitchenservice.repository.StaffMemberRepository;
import com.verimi.kitchenservice.repository.entity.StaffMember;
import com.verimi.kitchenservice.repository.entity.Vacation;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffMemberController {

    private static final Logger logger = LoggerFactory.getLogger(StaffMemberController.class);

    private final StaffMemberRepository repository;

    public StaffMemberController(StaffMemberRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/staff_members")
    List<StaffMember> all() {
        List<StaffMember> all = repository.findAll();
        for (StaffMember m : all) {
            logger.debug("{} {}, last time {}", m.getFirstName(), m.getLastName(), m.getLastService());
        }
        return all;
    }

    @PostMapping("/staff_members")
    StaffMember newStaffMember(@RequestBody StaffMember newStaffMember) {
        return repository.save(newStaffMember);
    }

    @GetMapping("/staff_members/{id}")
    StaffMember one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new StaffMemberNotFoundException(id));
    }

    @PutMapping("/staff_members/{id}")
    StaffMember replaceStaffMember(@RequestBody CreateStaffMemberRequestDTO updatedStaffMember, @PathVariable Long id) {
        return repository.findById(id)
                .map(staffMember -> {
                    staffMember.setLastService(updatedStaffMember.getLastService());
                    staffMember.setVacations(mapToVacations(updatedStaffMember.getVacations()));
                    return repository.save(staffMember);
                }).orElseThrow(() -> new StaffMemberNotFoundException(id));
    }

    private List<Vacation> mapToVacations(List<Date> vacations) {
        return vacations.stream().map(date -> Vacation.builder().vacationDate(date).build()).collect(Collectors.toList());
    }

    @DeleteMapping("/staff_members/{id}")
    void deleteStaffMember(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
