package com.delivery.authservice.controller;


import com.delivery.authservice.dto.UserDto;
import com.delivery.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "User",
        description = "User service"
)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/couriers")
    @Operation(summary = "Admin can see list of couriers with their statuses")
    public List<UserDto> getAllCouriers() {
        return userService.getAllCouriers();
    }
}
