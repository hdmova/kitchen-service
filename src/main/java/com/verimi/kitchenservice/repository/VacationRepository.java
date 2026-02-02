package com.verimi.kitchenservice.repository;

import com.verimi.kitchenservice.repository.entity.Vacation;
import org.springframework.data.repository.CrudRepository;

public interface VacationRepository extends CrudRepository<Vacation, Long> {
}
