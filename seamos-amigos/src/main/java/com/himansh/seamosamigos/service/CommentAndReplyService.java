package com.himansh.seamosamigos.service;

import com.himansh.seamosamigos.dto.CommentWebModel;
import com.himansh.seamosamigos.entity.Comments;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentAndReplyService {

	private final CommentRepository commentRepository;

	public CommentAndReplyService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public List<CommentWebModel> getAllComments(Integer photoId){
		return commentRepository.getAllCommentsByPicId(photoId).stream()
				.map(c->{
					c.setLikes(commentRepository.getTotalLikes(c.getCommentId()));
					return CommentWebModel.toWebModel(c);
				}).collect(Collectors.toList());
	}
	
	public CommentWebModel saveComment(CommentWebModel commentWebModel, int userId) {
		commentWebModel.setUserId(userId);
		if (commentWebModel.getCommentId()!=null && commentWebModel.getCommentId()!=0) {
			//commentWebModel.setPhotoId(null);
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
	
	public Integer deleteComment(Integer commentId, Integer userId) {
		Optional<Comments> comment= commentRepository.findById(commentId);
		if(comment.isPresent()) {
			Comments delComment= comment.get();
			boolean flag= false;
			if (userId == delComment.getUserId()) {
				flag= true;
			}
			else {
				Integer picId=delComment.getPhotoId();
				System.out.println(commentRepository.findUserIdByPicId(picId).size());
				if(commentRepository.findUserIdByPicId(picId).contains(userId))
					flag= true;
			}
			if(flag) {
				commentRepository.deleteById(commentId);
				return commentId;
			}
		}
		return 0;
	}

	public Object updateComment(CommentWebModel commentWebModel, Integer userId) throws InAppException {
		Integer commentId= commentWebModel.getCommentId();
		Optional<Comments> comment= commentRepository.findById(commentId);
		if (comment.isPresent()) {
			Comments updatesOnComent = comment.get();
			if (userId != updatesOnComent.getUserId()) {
				throw new InAppException("User not allowed to update");
			}
			//Integer likes= commentWebModel.getLikes();
			String message= commentWebModel.getBody();
			if(!StringUtils.isEmpty(message))
				updatesOnComent.setBody(commentWebModel.getBody());
			//else if(!StringUtils.isEmpty(likes+""))
			//	updatesOnComent.setLikes(commentWebModel.getLikes());
			else
				throw new InAppException("comment is empty");
			updatesOnComent= commentRepository.saveAndFlush(updatesOnComent);
			return updatesOnComent;
		}
		return null;
	}
}
