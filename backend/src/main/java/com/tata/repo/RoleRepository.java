package com.tata.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tata.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
