package com.himansh.seamosamigos.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.Photos;

public interface PhotoRepository extends PagingAndSortingRepository<Photos, Integer> {
	
	@Query("from photos p join p.users u where u.userId=:userId")
	public List<Photos> getAllProfilePotos(@Param("userId") int userId);
	
	@Query("select distinct p from photos p join p.users u where u.userId in"
			+ "(select c.user2.userId from connections c where c.user1.userId=:userId) order by p.dateOfUpload desc")
	Stream<Photos> getAllPotos(@Param("userId") int userId, Pageable pageable);

	@Query("select distinct p from photos p join p.users u where u.userId in"
			+ "(select c.user2.userId from connections c where c.user1.userId=:userId) and p.dateOfUpload < :timestamp order by p.dateOfUpload desc")
	Stream<Photos> getAllPotosByTimestamp(@Param("userId") int userId,@Param("timestamp") Date timestamp, Pageable pageable);

	@Query("select distinct p from photos p join p.users u where u.userId in"
			+ "(select c.user2.userId from connections c where c.user1.userId=:userId) and p.photoId < :picId order by p.dateOfUpload desc")
	Stream<Photos> getAllPotosByPhotoId(@Param("userId") int userId,@Param("picId") Integer picId, Pageable pageable);

	@Query("select count(lof.userId) from LikesOnFeeds lof where lof.feedId= :picId")
	public Integer getTotalLikes(@Param("picId") Integer picId);
	
	@Query("select max(p.photoId) from photos p")
	Integer getMaxPhotoId();
}
