package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.himansh.seamosamigos.entity.FollowRequests;

public interface RequestsRepository extends JpaRepository<FollowRequests, Integer> {
	@Query("from follow_request req where req.user1.userId=:user1 and req.user2.userId=:user2")
	public FollowRequests checkIfRequestAlreadyExists(@Param("user1") int user1,@Param("user2") int user2);

}
