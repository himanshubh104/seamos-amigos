package com.himansh.seamosamigos.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.himansh.seamosamigos.dto.CommentWebModel;
import com.himansh.seamosamigos.entity.Comments;
import com.himansh.seamosamigos.repository.CommentRepository;

@Service
public class CommentAndReplyService {
	@Autowired
	private CommentRepository commentRepository;
	
	public List<CommentWebModel> getAllComments(Integer photoId){
		return commentRepository.getAllCommentsByPicId(photoId).stream()
				.filter(c->c.getPhotoId()!=null && c.getPhotoId()!=0)
				.map(CommentWebModel::toWebModel).collect(Collectors.toList());
	}
	
	public CommentWebModel saveComment(CommentWebModel commentWebModel, int userId) {
		commentWebModel.setUserId(userId);
		if (commentWebModel.getCommentId()!=null && commentWebModel.getCommentId()!=0) {
			commentWebModel.setPhotoId(null);
			Optional<Comments> comment= commentRepository.findById(commentWebModel.getCommentId());
			if (comment.isPresent()) {
				Set<Comments> replies=comment.get().getReplies();
				replies.add(commentWebModel.toEntity());
				Comments entity= comment.get();
				entity.setReplies(replies);
				entity=commentRepository.saveAndFlush(entity);
				return CommentWebModel.toWebModel(entity);
			}
		}
		Comments comment= commentRepository.saveAndFlush(commentWebModel.toEntity());
		return CommentWebModel.toWebModel(comment);
	}
}
