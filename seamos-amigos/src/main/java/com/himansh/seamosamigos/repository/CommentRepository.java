package com.himansh.seamosamigos.repository;

import com.himansh.seamosamigos.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface CommentRepository extends JpaRepository<Comments, Integer> {
	@Query("from comments c where c.photoId= :picId and c.replyId is NULL")
	List<Comments> getAllCommentsByPicId(@Param("picId") Integer picId);
	
//	@Query("select u.userId from comments c, users u where u.userId=c.userId and c.photoId= :picId")
//	Set<Integer> findUserIdByPicId(@Param("picId") Integer picId);
	
	@Query(value= "select up.userId from user_photos up where up.photoId= :picId", nativeQuery = true)
	Set<Integer> findUserIdByPicId(@Param("picId") Integer picId);

	@Query("select lof.commentId, count(lof.userId) from LikesOnFeeds lof where lof.commentId in :commentIds group by lof.commentId")
	Stream<Object[]> getTotalLikesForComments(List<Integer> commentIds);
}
