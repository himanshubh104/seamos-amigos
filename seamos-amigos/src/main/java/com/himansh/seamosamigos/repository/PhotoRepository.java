package com.himansh.seamosamigos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.Photos;

public interface PhotoRepository extends JpaRepository<Photos, Integer> {
	
	@Query("from photos p join p.users u where u.userId=:userId")
	public List<Photos> getAllProfilePotos(@Param("userId") int userId);
	
	@Query("from photos p join p.users u where u.userId in"
			+ "(select c.user2.userId from connections c where c.user1.userId=:userId) order by p.dateOfUpload desc")
	public List<Photos> getAllPotos(@Param("userId") int userId);

}
