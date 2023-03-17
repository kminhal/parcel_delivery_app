package com.delivery.authservice.service;


import com.delivery.authservice.dto.JwtResponse;
import com.delivery.authservice.dto.SigninRequest;
import com.delivery.authservice.dto.SignupRequest;
import com.delivery.authservice.model.Role;
import com.delivery.authservice.model.RoleName;
import com.delivery.authservice.model.User;
import com.delivery.authservice.repository.RoleRepository;
import com.delivery.authservice.repository.UserRepository;
import com.delivery.authservice.security.JwtTokenCreator;
import com.delivery.authservice.util.exception.RegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenCreator jwtTokenCreator;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenCreator jwtTokenCreator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenCreator = jwtTokenCreator;
    }

    public JwtResponse signin(SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getUsername(),
                        signinRequest.getPassword()
                )
        );
        String jwt = jwtTokenCreator.generateToken(authentication);
        log.info("User {} has logged in", signinRequest.getUsername());
        return new JwtResponse(jwt);
    }

    @Transactional
    public void signup(SignupRequest signupRequest) {
        signup(signupRequest, RoleName.ROLE_USER);
    }

    @Transactional
    public void signupCourier(SignupRequest signupRequest) {
        signup(signupRequest, RoleName.ROLE_COURIER);
    }

    private void signup(SignupRequest signupRequest, RoleName roleName) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RegistrationException("Username " + signupRequest.getUsername() + " is already used");
        }
        Role userRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RegistrationException("User role is not set or such role does not exist"));

        User user = User.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .roles(Collections.singleton(userRole))
                .build();

        log.info("User {} successfully registered", user.getUsername());
        userRepository.save(user);
    }
}
