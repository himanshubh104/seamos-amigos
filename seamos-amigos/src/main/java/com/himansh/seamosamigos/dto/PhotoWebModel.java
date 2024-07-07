package com.himansh.seamosamigos.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.himansh.seamosamigos.entity.Photo;
import com.himansh.seamosamigos.utility.AmigosUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoWebModel {
	private int photoId;
	private String url;
	private String caption;
	private int likes;
	private String dateOfUpload;

	public static List<PhotoWebModel> toWebModels(Stream<Photo> picStream) {
		AmigosUtils util= new AmigosUtils();
		try (picStream) {
			return picStream.map(p->{
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
}
