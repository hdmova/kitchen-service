package com.verimi.kitchenservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StaffMemberRepository extends CrudRepository<StaffMember, Long> {
    List<StaffMember> findAll();
}
