package com.himansh.seamosamigos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.PhotoWebModel;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.service.CommentAndReplyService;
import com.himansh.seamosamigos.service.PhotoService;
import com.himansh.seamosamigos.utility.CurrentUser;

@RestController
@RequestMapping("api/seamos-amigos/")
public class MediaController {
	@Autowired
	private PhotoService photoService;
	@Autowired
	private CommentAndReplyService commentService;
	private int userId=-1;
	
	
    @ModelAttribute
    public void fetchUser() {
    	userId = CurrentUser.getCurrentUserId();
    }
    /*-----------------------------------------------Photos-------------------------------------------------------*/
	@PostMapping(path = "media/add-photo")
	public PhotoDto addUserPhoto(@ModelAttribute PhotoDto photoDto) throws Exception {
		PhotoDto pic=photoService.addUserPhoto(photoDto, userId);
		pic.setPicData(null);
		return pic;
	}
	
	@GetMapping(value = "media/get-photo", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getImage(@RequestParam String picPath) throws IOException, InAppException {
	    return ResponseEntity.ok(photoService.getImage(picPath));
	}
	
	@GetMapping(path = "media/photos")
	public List<Photos> getUserPhotos(){
		return photoService.getUserPhotos(userId);		
	}
	@GetMapping(path = "media/feeds/photos-by-id")
	public List<PhotoWebModel> getFeedsByPicId(@RequestParam(name="picId", required = false) Integer picId){
		if(picId==null) {
			return photoService.getHomeScreenPhotos(userId);
		}
		return photoService.getFeedsByPicId(userId, picId);		
	}
	
	@GetMapping(path = "media/feeds/photos")
	public List<PhotoWebModel> getFeedsPhotos(@RequestParam(name = "timestamp", required = false) String timestamp) throws Exception{
		if (StringUtils .isEmpty(timestamp)) {
			return photoService.getHomeScreenPhotos(userId);
		}
		return photoService.getFeedsBytimestamp(userId, timestamp);		
	}
	
	@GetMapping(path = "media/feed/comments")
	public List<CommentWebModel> getPhotoComments(@RequestParam(name = "photoId") Integer photoId) throws Exception{
		return commentService.getAllComments(photoId);		
	}
	
	@PostMapping(path = "media/feed/add-comment")
	public Boolean addComment(@RequestBody CommentWebModel commentWebModel) throws Exception{
		return commentService.saveComment(commentWebModel, userId) !=null;		
	}
	
	@PutMapping(path = "media/feed/update-comment")
	public Boolean updateComment(@RequestBody CommentWebModel commentWebModel) throws InAppException{
		return commentService.updateComment(commentWebModel, userId) !=null;		
	}
	
	@DeleteMapping(path = "media/feed/delete-comment")
	public String deleteComment(@RequestParam(name = "commentId") Integer commentId) throws Exception{
		return commentService.deleteComment(commentId, userId)>0?"Deleted":"Comment Not Deleted";		
	}
}
