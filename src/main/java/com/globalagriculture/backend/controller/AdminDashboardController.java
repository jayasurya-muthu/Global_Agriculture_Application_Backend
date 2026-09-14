package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.dto.DashboardStatistics;
import com.globalagriculture.backend.service.AdminService;
import org.springframework.web.bind.annotation.*;

// Frontend: src/api/admin.js -> GET /admin/dashboard/statistics
@RestController
@RequestMapping("/admin/dashboard")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminDashboardController {

    private final AdminService adminService;

    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/statistics")
    public DashboardStatistics getStatistics() {
        return adminService.getStatistics();
    }
}
