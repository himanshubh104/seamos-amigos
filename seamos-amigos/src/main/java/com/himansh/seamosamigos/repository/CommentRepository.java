package com.himansh.seamosamigos.repository;

import com.himansh.seamosamigos.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	@Query("from comments c where c.photoId= :picId and c.replyId is NULL")
	List<Comment> getAllCommentsByPicId(@Param("picId") Integer picId);
	
//	@Query("select u.userId from comments c, users u where u.userId=c.userId and c.photoId= :picId")
//	Set<Integer> findUserIdByPicId(@Param("picId") Integer picId);
	
	@Query(value= "select up.userId from user_photos up where up.photo_id= :picId", nativeQuery = true)
	Set<Integer> findUserIdByPicId(@Param("picId") Integer picId);

	@Query("select lof.feedId, count(lof.likeId) from LikeOnFeed lof "
			+ "where lof.feedId in :commentIds and lof.feedType = 'COMMENT' group by lof.feedId")
	Stream<Object[]> getTotalLikesForComments(List<Integer> commentIds);
}
