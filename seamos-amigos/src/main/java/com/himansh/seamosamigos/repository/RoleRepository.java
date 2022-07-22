package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.himansh.seamosamigos.entity.Roles;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Roles, Integer> {
	Roles findByRoleName(String name);

	@Query("from Roles r where r.roleName in :roles")
	List<Roles> findAllByRoleNames(List<String> roles);
}
