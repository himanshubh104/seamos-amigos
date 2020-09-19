package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.Connections;

public interface ConnectionRepository extends JpaRepository<Connections, Integer> {
	@Query("from connections c where c.user1.userId=:user1 and c.user2.userId=:user2")
	public Connections checkConnection(@Param("user1") int user1,@Param("user2") int user2);
}
