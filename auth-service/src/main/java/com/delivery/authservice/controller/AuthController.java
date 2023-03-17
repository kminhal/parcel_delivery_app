package com.delivery.authservice.controller;


import com.delivery.authservice.dto.JwtResponse;
import com.delivery.authservice.dto.MessageResponse;
import com.delivery.authservice.dto.SigninRequest;
import com.delivery.authservice.dto.SignupRequest;
import com.delivery.authservice.service.AuthService;
import com.delivery.authservice.util.exception.RegistrationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Auth",
        description = "Sign in and sign up service"
)
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    @Operation(summary = "User, Admin, Courier can log in")
    public ResponseEntity<JwtResponse> signin(@Valid @RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authService.signin(signinRequest));
    }

    @PostMapping("/signup")
    @Operation(summary = "User can create an user account")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok(new MessageResponse("User CREATED"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signup/courier")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Admin can create an courier account")
    public ResponseEntity<?> signupCourier(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signupCourier(signupRequest);
        return ResponseEntity.ok(new MessageResponse("Courier CREATED"));
    }

    @ExceptionHandler
    private ResponseEntity<MessageResponse> catchRegistrationException(RegistrationException e) {
        return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
