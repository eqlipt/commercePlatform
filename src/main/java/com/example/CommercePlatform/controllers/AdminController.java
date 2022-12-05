//package com.example.CommercePlatform.controllers;
//
//import com.example.CommercePlatform.services.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
////@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
//public class AdminController {
//    private final AdminService adminService;
//
//    @Autowired
//    public AdminController(AdminService adminService) {
//        this.adminService = adminService;
//    }
//
//    @GetMapping("/admin")
//    public String adminPage() {
//        return "personal";
//    }
//
//
//}
