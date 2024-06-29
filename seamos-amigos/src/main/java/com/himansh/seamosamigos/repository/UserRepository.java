package com.himansh.seamosamigos.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String email);
	
	@Query("from User u where u.userId in :userIds")
	List<User> getListOfUsers(@Param("userIds") int[] userIds);
	
//	@Query("from User u where u.userId in(select c.user2Id from connection c where c.user1.userId=:userid)")
//	public List<UserEntity> getConnectionList(@Param("userid") int userid);
	
	@Query("select c.user2 from connections c where c.user1.userId=:userid")
	List<User> getFollowers(@Param("userid") int userid);
	
	@Query("select c.user1 from connections c where c.user2.userId=:userid")
	List<User> getFollowings(@Param("userid") int userid);
	
	@Transactional
	@Modifying
	@Query("update User u set u.activeSessions = u.activeSessions+1 where u.userId = :userId")
	Integer addLoginSession(Integer userId);

}
