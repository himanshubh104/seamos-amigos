package com.himansh.seamosamigos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	public UserEntity findByEmail(String email);
	
	@Query("from users u where u.userId=userid")
	public List<UserEntity> getListOfUsers(@Param("userid") int userid);
}
