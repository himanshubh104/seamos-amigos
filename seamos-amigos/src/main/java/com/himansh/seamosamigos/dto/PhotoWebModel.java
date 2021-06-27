package com.himansh.seamosamigos.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.utility.AmigosUtils;

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
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caption == null) ? 0 : caption.hashCode());
		result = prime * result + ((dateOfUpload == null) ? 0 : dateOfUpload.hashCode());
		result = prime * result + likes;
		result = prime * result + photoId;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		PhotoWebModel other = (PhotoWebModel) obj;
		if (caption == null) {
			if (other.caption != null)
				return false;
		} else if (!caption.equals(other.caption))
			return false;
		if (dateOfUpload == null) {
			if (other.dateOfUpload != null)
				return false;
		} else if (!dateOfUpload.equals(other.dateOfUpload))
			return false;
		if (likes != other.likes)
			return false;
		if (photoId != other.photoId)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	public static List<PhotoWebModel> findWebModels(List<Photos> set) {
		AmigosUtils util= new AmigosUtils();
		return set.stream().map(p->{
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
