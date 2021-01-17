package com.himansh.seamosamigos.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.utility.Utilities;

public class PhotoWebModel {
	private int photoId;
	private String url;
	private String caption;
	private int likes;
	private String dateOfUpload;
	
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
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public String getDateOfUpload() {
		return dateOfUpload;
	}
	public void setDateOfUpload(String dateOfUpload) {
		this.dateOfUpload = dateOfUpload;
	}
	
	public static List<PhotoWebModel> findWebModels(List<Photos> photos) {
		Utilities util= new Utilities();
		return photos.stream().map(p->{
			PhotoWebModel model=new PhotoWebModel();
			model.setPhotoId(p.getPhotoId());
			model.setCaption(p.getCaption());
			try {
				model.setDateOfUpload(util.dateToString(p.getDateOfUpload()));
			} catch (Exception e) {
				model.setDateOfUpload(null);
			}
			model.setLikes(p.getLikes());
			model.setUrl(p.getUrl());
			return model;
		}).collect(Collectors.toList());
	}
}
