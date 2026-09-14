package com.globalagriculture.backend.controller;

import com.globalagriculture.backend.entity.User;
import com.globalagriculture.backend.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Frontend: src/api/admin.js -> GET /admin/users
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }
}
