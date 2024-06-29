package com.himansh.seamosamigos.repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.Photo;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, Integer> {
	
	@Query("from Photo p join p.users u where u.userId=:userId")
	public List<Photo> getAllProfilePhotos(@Param("userId") int userId);

	@Query("select distinct p from Photo p join p.users u join connections c on u.userId = c.user2.userId " +
			"where c.user1.userId=:userId or u.userId = :userId order by p.dateOfUpload desc")
	Stream<Photo> getAllPhotos(@Param("userId") int userId, Pageable pageable);

	@Query("select distinct p from Photo p join p.users u join connections c on u.userId = c.user2.userId " +
			"where (c.user1.userId=:userId or u.userId = :userId) and p.dateOfUpload < :timestamp order by p.dateOfUpload desc")
	Stream<Photo> getAllPhotosByTimestamp(@Param("userId") int userId, @Param("timestamp") Date timestamp, Pageable pageable);

	@Query("select distinct p from Photo p join p.users u join connections c on u.userId = c.user2.userId " +
			"where (c.user1.userId=:userId or u.userId = :userId) and p.photoId < :picId order by p.dateOfUpload desc")
	Stream<Photo> getAllPhotosByPhotoId(@Param("userId") int userId, @Param("picId") Integer picId, Pageable pageable);
	
	@Query("select max(p.photoId) from Photo p")
	Integer getMaxPhotoId();
}
