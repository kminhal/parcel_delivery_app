package com.delivery.authservice.repository;

import com.delivery.authservice.model.RoleName;
import com.delivery.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select distinct user from User user left join fetch user.roles role where role.name = :role")
    List<User> findAllByRole(RoleName role);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
