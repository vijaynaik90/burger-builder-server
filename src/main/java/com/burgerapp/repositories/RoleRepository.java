package com.burgerapp.repositories;

import com.burgerapp.domain.Role;
import com.burgerapp.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName name);
}
