package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.himansh.seamosamigos.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Roles findByRoleName(String name);
}
