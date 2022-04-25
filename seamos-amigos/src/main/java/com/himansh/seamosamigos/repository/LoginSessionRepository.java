package com.himansh.seamosamigos.repository;

import java.util.stream.Stream;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.himansh.seamosamigos.entity.LoginSession;

public interface LoginSessionRepository extends JpaRepository<LoginSession, Long> {
	
	@Transactional
	@Modifying
	Integer deleteByUserIp(String userIp);
		
	LoginSession getByUserIdAndUserIp(Integer userId, String clientIp);
	
	int countByLoginId(Long loginId);

	@Query("select ls.userIp as user_ip, ls.userAgent as user_agent, ls.loginTime as login_time from LoginSession ls where ls.userId = :userId")
	Stream<Tuple> getUserIpAndAgents(int userId);

}
