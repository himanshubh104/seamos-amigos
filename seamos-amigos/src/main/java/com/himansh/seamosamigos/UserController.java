package com.himansh.seamosamigos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.UserDto;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.service.UserService;

@RestController
@RequestMapping("api/seamos-amigos/")
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping(path = "users",consumes = "application/JSON")
	public UserDto addUser(@RequestBody UserDto user) {
		return userService.addUser(user.generateEntity());	
	}
	
	@PostMapping(path = "users/photos")
	public PhotoDto addUserPhoto(@ModelAttribute PhotoDto photoDto) throws Exception {
		PhotoDto pic=userService.addUserPhoto(photoDto);
		pic.setPicData(null);
		return pic;
	}
	
	@GetMapping(path = "users/photos/{userId}")
	public List<Photos> getUserPhotos(@PathVariable(name = "userId") int userId){
		return userService.getUserPhotos(userId);		
	}

}
