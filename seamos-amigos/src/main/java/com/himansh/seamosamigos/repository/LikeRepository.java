package com.himansh.seamosamigos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.LikeOnFeed;

import javax.transaction.Transactional;

public interface LikeRepository extends JpaRepository<LikeOnFeed, Integer>{
	@Query("select count(lof) from LikeOnFeed lof where lof.feedId= :feedId")
	public Long getTotalLikes(@Param("feedId") Integer feedId);

	@Query("select count(lof) from LikeOnFeed lof where lof.feedId= :feedId and lof.userId = :userId")
	Integer isLikedAlready(Integer feedId, Integer userId);

	@Transactional
	@Modifying
	@Query("delete from LikeOnFeed lof where lof.feedId = :feedId")
	Integer deleteByFeedId(Integer feedId);
}
