package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.LikesOnFeeds;

public interface LikesRepository extends JpaRepository<LikesOnFeeds, Long>{
	@Query("select count(lof) from LikesOnFeeds lof where lof.feedId= :picId")
	public Integer getTotalLikes(@Param("picId") Long picId);
	
}
