package com.verimi.kitchenservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        for(StaffMember m : all) {
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
    StaffMember replaceStaffMember(@RequestBody StaffMember newStaffMember, @PathVariable Long id) {
        return repository.findById(id)
                .map(staffMember -> {
                    staffMember.setLastService(newStaffMember.getLastService());
                    return repository.save(staffMember);
                }).orElseThrow(() -> new StaffMemberNotFoundException(id));
    }

    @DeleteMapping("/staff_members/{id}")
    void deleteStaffMember(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
