package com.himansh.seamosamigos.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import com.himansh.seamosamigos.entity.Photo;

@Setter
@Getter
public class PhotoDto {
	private int photoId;
	private String url;
	private MultipartFile picData;
	private String caption;
	private int userId;
	
	public Photo generatePhotoEntity() {
		Photo photo =new Photo();
		photo.setCaption(caption);
		return photo;
	}
	
	public static PhotoDto generateDto(Photo photo) {
		PhotoDto dto=new PhotoDto();
		dto.setCaption(photo.getCaption());
		dto.setUrl(photo.getUrl());
		dto.setPhotoId(photo.getPhotoId());
		return dto;
	}
	
}
