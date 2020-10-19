package com.himansh.seamosamigos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.himansh.seamosamigos.entity.FollowRequests;

public interface RequestsRepository extends JpaRepository<FollowRequests, Integer> {
	@Query("from follow_request req where req.requestedUser.userId=:requestedUser and req.requestingUser.userId=:requestingUser")
	public FollowRequests checkIfRequestAlreadyExists(@Param("requestedUser") int requestedUser,@Param("requestingUser") int requestingUser);

	@Query("from follow_request req where req.requestedUser.userId=:requestedUser")
	public List<FollowRequests> getAllRequests(@Param("requestedUser")int requestedUser);
}
