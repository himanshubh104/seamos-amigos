package com.himansh.seamosamigos.controller;

import java.util.List;

import com.himansh.seamosamigos.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.dto.CommentWebModel;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.CommentAndReplyService;
import com.himansh.seamosamigos.utility.CurrentUser;

@RestController
@RequestMapping("api/seamos-amigos/")
public class CommentLikeController {
	private final CommentAndReplyService commentService;
	private final LikeService likeService;
	private final Logger log = LoggerFactory.getLogger(CommentLikeController.class);
	private Integer userId = -1;

	public CommentLikeController(CommentAndReplyService commentService, LikeService likeService) {
		this.commentService = commentService;
		this.likeService = likeService;
	}
	
	@ModelAttribute
    public void fetchUser() {
    	userId  = CurrentUser.getCurrentUserId();
    }
	
	@GetMapping(path = "media/feed/comments")
	public List<CommentWebModel> getPhotoComments(@RequestParam(name = "photoId") Integer photoId) throws Exception{
		log.info("Request for: media/feed/comments");
		return commentService.getAllComments(photoId);		
	}
	
	@PostMapping(path = "media/feed/add-comment")
	public Boolean addComment(@RequestBody CommentWebModel commentWebModel) throws Exception{
		log.info("Request for: media/feed/add-comment");
		return commentService.saveComment(commentWebModel, userId) !=null;		
	}
	
	@PutMapping(path = "media/feed/update-comment")
	public Boolean updateComment(@RequestBody CommentWebModel commentWebModel) throws InAppException{
		log.info("Request for: media/feed/update-comment");
		return commentService.updateComment(commentWebModel, userId) !=null;		
	}
	
	@DeleteMapping(path = "media/feed/delete-comment")
	public String deleteComment(@RequestParam(name = "commentId") Integer commentId) throws Exception{
		log.info("Request for: media/feed/delete-comment");
		return commentService.deleteComment(commentId, userId)>0?"Deleted":"Comment Not Deleted";		
	}
	@PostMapping(path = "media/feed/give-like")
	public Integer giveLike(@RequestParam(name = "feedId") Integer feedId, @RequestParam(name = "feedType") String feedType) throws Exception{
		log.info("Request for: media/feed/give-like");
		return likeService.addLike(feedId, userId, feedType);
	}

	@PostMapping(path = "media/feed/disLike")
	public Long disLike(@RequestParam(name = "feedId") Integer feedId, @RequestParam(name = "feedType") String feedType) throws Exception{
		log.info("Request for: media/feed/disLike");
		return likeService.disLike(feedId, feedType);
	}

	@GetMapping(path = "media/feed/have-user-like")
	public Boolean haveUserLiked(@RequestParam(name = "feedId") Integer feedId) throws Exception{
		log.info("Request for: media/feed/have-user-like");
		return likeService.haveUserLiked(feedId, userId);
	}

}
