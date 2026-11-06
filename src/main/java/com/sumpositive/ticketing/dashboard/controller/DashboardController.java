package com.sumpositive.ticketing.dashboard.controller;


import com.sumpositive.ticketing.dashboard.dto.Dashboard;
import com.sumpositive.ticketing.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
		super();
		this.dashboardService = dashboardService;
	}

	@GetMapping("/summary")
    public Dashboard getSummary() {
        return dashboardService.getSummary();
    }
}
