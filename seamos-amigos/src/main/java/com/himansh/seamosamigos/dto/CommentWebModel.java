package com.himansh.seamosamigos.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Comments;
import com.himansh.seamosamigos.utility.AmigosUtils;

public class CommentWebModel implements Serializable{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer commentId;
	private String body;
	private Integer likes;
	private Integer userId;
	private Integer photoId;
	private String timeStamp;
	private Set<CommentWebModel> replies;
	
	
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Set<CommentWebModel> getReplies() {
		return replies;
	}
	public void setReplies(Set<CommentWebModel> replies) {
		this.replies = replies;
	}
	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public Integer getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
		result = prime * result + ((likes == null) ? 0 : likes.hashCode());
		result = prime * result + ((photoId == null) ? 0 : photoId.hashCode());
		result = prime * result + ((replies == null) ? 0 : replies.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentWebModel other = (CommentWebModel) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (commentId == null) {
			if (other.commentId != null)
				return false;
		} else if (!commentId.equals(other.commentId))
			return false;
		if (likes == null) {
			if (other.likes != null)
				return false;
		} else if (!likes.equals(other.likes))
			return false;
		if (photoId == null) {
			if (other.photoId != null)
				return false;
		} else if (!photoId.equals(other.photoId))
			return false;
		if (replies == null) {
			if (other.replies != null)
				return false;
		} else if (!replies.equals(other.replies))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	public Comments toEntity() {
		Comments comment=new Comments();
		comment.setBody(body);
		comment.setLikes(likes);
		if(photoId!=null && photoId!=0)
			comment.setPhotoId(photoId);
		comment.setTimeStamp(Calendar.getInstance().getTime());
		comment.setUserId(userId);
		return comment;
	}
	
	public static CommentWebModel toWebModel(Comments entity) {
		//System.out.println("I got replyId "+entity.getReplyId());
		AmigosUtils util= new AmigosUtils();
		CommentWebModel model=new CommentWebModel();
		model.setBody(entity.getBody());
		model.setCommentId(entity.getCommentId());
		model.setLikes(entity.getLikes());
		model.setPhotoId(entity.getPhotoId());
		model.setUserId(entity.getUserId());
		try {
			model.setTimeStamp(util.dateToString(entity.getTimeStamp()));
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
					model1.setTimeStamp(util.dateToString(e.getTimeStamp()));
				} catch (Exception exc) {
					model1.setTimeStamp(null);
				}
				return model1;
			}).collect(Collectors.toSet()));
		return model;
	}
	
}
