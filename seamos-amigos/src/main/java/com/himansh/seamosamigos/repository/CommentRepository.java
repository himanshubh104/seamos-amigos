package com.himansh.seamosamigos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.himansh.seamosamigos.entity.Comments;

public interface CommentRepository extends JpaRepository<Comments, Integer> {
	@Query("from comments c where c.photoId= :picId")
	List<Comments> getAllCommentsByPicId(@Param("picId") Integer picId);
}
