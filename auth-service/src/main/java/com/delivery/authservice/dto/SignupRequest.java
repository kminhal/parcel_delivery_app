package com.delivery.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @Email
    private String username;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Override
    public String toString() {
        return "SigninRequest{" +
                "username='" + username + '\'' +
                ", password='***'" +
                ", firstName='" + firstName + '\''+
                ", lastName='" + lastName + '\''+
                '}';
    }
}
