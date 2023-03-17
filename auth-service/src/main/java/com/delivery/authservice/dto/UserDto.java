package com.delivery.authservice.dto;

import com.delivery.authservice.model.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private Long id;

    @Email
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && username.equals(userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
