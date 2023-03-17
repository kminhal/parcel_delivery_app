package com.delivery.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Override
    public String toString() {
        return "SigninRequest{" +
                "username='" + username + '\'' +
                ", password='***'" +
                '}';
    }
}
