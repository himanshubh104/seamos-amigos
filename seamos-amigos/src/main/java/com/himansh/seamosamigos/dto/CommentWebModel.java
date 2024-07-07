package com.himansh.seamosamigos.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Comment;
import com.himansh.seamosamigos.utility.AmigosUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(exclude = "replies")
public class CommentWebModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer commentId;
	private String body;
	private Integer likes;
	private Integer userId;
	private Integer photoId;
	private String timeStamp;
	private Set<CommentWebModel> replies;
	
	public Comment toEntity() {
		Comment comment=new Comment();
		comment.setBody(body);
		comment.setLikes(likes);
		if(photoId!=null && photoId!=0)
			comment.setPhotoId(photoId);
		comment.setTimestamp(Calendar.getInstance().getTime());
		comment.setUserId(userId);
		return comment;
	}
	
	public static CommentWebModel toWebModel(Comment entity) {
		//System.out.println("I got replyId "+entity.getReplyId());
		AmigosUtils util= new AmigosUtils();
		CommentWebModel model=new CommentWebModel();
		model.setBody(entity.getBody());
		model.setCommentId(entity.getCommentId());
		model.setLikes(entity.getLikes());
		model.setPhotoId(entity.getPhotoId());
		model.setUserId(entity.getUserId());
		try {
			model.setTimeStamp(util.dateToString(entity.getTimestamp()));
		} catch (Exception e) {
			model.setTimeStamp(null);
		}
		if (entity.getReplies()!=null)
			model.setReplies(entity.getReplies().stream().map(e->{
				CommentWebModel model1=new CommentWebModel();
				model1.setBody(e.getBody());
				model1.setCommentId(e.getCommentId());
				model1.setLikes(e.getLikes());
				model1.setPhotoId(entity.getPhotoId());
				model1.setUserId(e.getUserId());
				try {
					model1.setTimeStamp(util.dateToString(e.getTimestamp()));
				} catch (Exception exc) {
					model1.setTimeStamp(null);
				}
				return model1;
			}).collect(Collectors.toSet()));
		return model;
	}
	
}
