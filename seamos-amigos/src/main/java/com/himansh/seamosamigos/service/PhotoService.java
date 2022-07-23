package com.himansh.seamosamigos.service;

import com.himansh.seamosamigos.dto.PhotoDto;
import com.himansh.seamosamigos.dto.PhotoWebModel;
import com.himansh.seamosamigos.entity.Photos;
import com.himansh.seamosamigos.exception.InAppException;
import com.himansh.seamosamigos.repository.PhotoRepository;
import com.himansh.seamosamigos.repository.UserRepository;
import com.himansh.seamosamigos.utility.AmigosUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PhotoService {
	private final UserRepository userRepository;
	private final PhotoRepository photoRepository;
	private final AmigosUtils utility;

	public PhotoService(UserRepository userRepository, PhotoRepository photoRepository, AmigosUtils utility) {
		this.userRepository = userRepository;
		this.photoRepository = photoRepository;
		this.utility = utility;
	}

	public List<Photos> getUserPhotos(int userid){															// Get Profile photos
		return photoRepository.getAllProfilePotos(userid);	
	}
	
	public List<PhotoWebModel> getFeedsByPicId(int userid, int picId){										// Get photos in Feeds
		Pageable pageable= PageRequest.of(0, 10);
		return PhotoWebModel.toWebModels(photoRepository.getAllPotosByPhotoId(userid, picId, pageable));	
	}
	
	@Transactional(readOnly = true)
	public List<PhotoWebModel> getHomeScreenPhotos(int userId) {
		Pageable pageable= PageRequest.of(0, 10);
		Stream<Photos> allPotos = photoRepository.getAllPotos(userId, pageable);
		return PhotoWebModel.toWebModels(allPotos);
	}
	
	@Transactional(readOnly = true)
	public List<PhotoWebModel> getFeedsBytimestamp(int userid, String timestamp) throws Exception{			// Get photos in Feeds.
		Pageable pageable= PageRequest.of(0, 10);
		return PhotoWebModel.toWebModels(
				photoRepository.getAllPotosByTimestamp(userid, utility.stringToDateTime(timestamp), pageable));	
	}
	
	public PhotoDto addUserPhoto(PhotoDto photo, int userId) throws Exception{								// Upload a picture
		Integer lastPicId = photoRepository.getMaxPhotoId();
		lastPicId = lastPicId==null? 0 : lastPicId;
		String uri=utility.saveUploadedFile(photo.getPicData(), userId, lastPicId);
		if (uri!=null) {
			Photos photoEntity=photo.generatePhotoEntity();
			photoEntity.setUrl(uri);
			photoEntity.setDateOfUpload(Calendar.getInstance().getTime());
			photoEntity.setUsers(userRepository.getListOfUsers(new int[]{userId}));
			return PhotoDto.generateDto(photoRepository.save(photoEntity));	
		}
		return photo;
	}
	
	public byte[] getImage(String picPath) throws InAppException, IOException {
		try {
			var picInBytes = Files.readAllBytes(Paths.get(picPath));
		    return picInBytes;
		} catch(NoSuchFileException ex) {
			throw new InAppException("File not found!");
		}
	}

}
