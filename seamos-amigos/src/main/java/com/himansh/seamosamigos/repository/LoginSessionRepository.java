package com.himansh.seamosamigos.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.himansh.seamosamigos.entity.LoginSession;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Integer> {
	
	@Transactional
	@Modifying
	Integer deleteByUserIp(String userIp);
	
	@Query("select count(1) from LoginSession ls where ls.userIp = :userIp")
	Integer checkAlreadyLoggedIn(String userIp);

}
