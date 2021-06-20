package com.himansh.seamosamigos.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.himansh.seamosamigos.entity.Photos;

public class PhotoDto {
	private int photoId;
	private String url;
	private MultipartFile picData;
	private String caption;
	private int userId;
	
	@JsonIgnore
	public MultipartFile getPicData() {
		return picData;
	}
	public void setPicData(MultipartFile picData) {
		this.picData = picData;
	}
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public Photos generatePhotoEntity() {
		Photos photo =new Photos();
		photo.setCaption(caption);
		return photo;
	}
	
	public static PhotoDto generateDto(Photos photo) {
		PhotoDto dto=new PhotoDto();
		dto.setCaption(photo.getCaption());
		dto.setUrl(photo.getUrl());
		dto.setPhotoId(photo.getPhotoId());
		return dto;
	}
	
}
