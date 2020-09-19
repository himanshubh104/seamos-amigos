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
	
//	@Query("from users u where u.userId in(select c.user2Id from connection c where c.user1.userId=:userid)")
//	public List<UserEntity> getConnectionList(@Param("userid") int userid);
	
	@Query("select c.user2 from connections c where c.user1.userId=:userid")
	public List<UserEntity> getFollowers(@Param("userid") int userid);
	
	@Query("select c.user1 from connections c where c.user2.userId=:userid")
	public List<UserEntity> getFollowings(@Param("userid") int userid);
}
