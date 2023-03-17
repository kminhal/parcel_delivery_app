package com.delivery.authservice.service;


import com.delivery.authservice.dto.UserDto;
import com.delivery.authservice.model.RoleName;
import com.delivery.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public List<UserDto> getAllCouriers() {
        return userRepository
                .findAllByRole(RoleName.ROLE_COURIER)
                .stream()
                .map(user-> mapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }
}
