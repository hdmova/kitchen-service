package com.verimi.kitchenservice.repository;

import com.verimi.kitchenservice.repository.entity.StaffMember;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface StaffMemberRepository extends CrudRepository<StaffMember, Long> {
    List<StaffMember> findAll();
}
