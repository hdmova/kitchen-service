package com.verimi.kitchenservice.controller;

import com.verimi.kitchenservice.model.GetWeekPlanRequestDTO;
import com.verimi.kitchenservice.repository.entity.DayOfService;
import com.verimi.kitchenservice.service.WeekPlanService;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeekPlanController {

    private static final Logger logger = LoggerFactory.getLogger(WeekPlanController.class);

    private final WeekPlanService weekPlanService;

    public WeekPlanController(WeekPlanService weekPlanService) {
        this.weekPlanService = weekPlanService;
    }

    @PostMapping("/week-plan")
    ResponseEntity<List<DayOfService>> createKitchenWeekPlan(@RequestBody @Valid GetWeekPlanRequestDTO weekPlanRequestDTO) {
        return ResponseEntity.ok(weekPlanService.createWeekPlan(weekPlanRequestDTO));
    }
}