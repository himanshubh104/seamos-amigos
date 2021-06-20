package com.himansh.seamosamigos.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.himansh.seamosamigos.entity.LoginSession;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Integer> {
	
	@Transactional
	@Modifying
	Integer deleteByUserIp(String userIp);
		
	LoginSession getByUserIdAndUserIp(Integer userId, String clientIp);

}
