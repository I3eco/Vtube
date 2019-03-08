package com.vtube.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
//import java.util.Map;
import java.util.NoSuchElementException;

//import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import com.vtube.dal.VideosRepository;
import com.vtube.dto.VideoToSaveDTO;
import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.Video;


/**
 * Class to manage database with video related requests.
 * 
 * @author I3eco
 *
 */
@Service
public class VideoService {
	private static final String VIDEO_DIR_NAME = "Video";
	private static final String THUMBNAIL_DIR_NAME = "Picture";
	private static final String SUPPORTED_VIDEO_FORMATS = "mp4 avi wmv";
	private static final String SUPPORTED_THUMBNAIL_FORMATS = "jpeg jpg png gif";
	private static final String UPLOAD_DIR = "E:\\IT Talents Java Course\\VTubeFileStorage\\";
	
//	private static final Cloudinary cloudinary = new Cloudinary
//			("CLOUDINARY_URL=cloudinary://689344796343136:jpv4nWOWuPXqg-fYL0NFfxpAxVE@vtubeto");

	
	@Autowired
	private VideosRepository videosRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public boolean findById(Long videoId) {
		try {
			videosRepository.findById(videoId).get();
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	public Video getVideoById(Integer videoId) {
		return this.videosRepository.findById(videoId).get();
	}
	
	public void uploadVideoData(MultipartFile file, MultipartFile thumbnail, String title, String description, Long ownerId, Channel channel) throws FileExistsException, VideoNotFoundException, UnsupportedFileFormatException {
		this.checkFileFormat(file, SUPPORTED_VIDEO_FORMATS, "Video");
		this.checkFileFormat(thumbnail, SUPPORTED_THUMBNAIL_FORMATS, "Picture");
		
		VideoToSaveDTO videoToSave = new VideoToSaveDTO();
		videoToSave.setDescription(description);
		if(title == null || title.trim().equals("")) {
			videoToSave.setTitle(FilenameUtils.removeExtension(file.getOriginalFilename()));
		} else {
			videoToSave.setTitle(title);
		}
		
		String videoUrl = "";
		String thumbnailUrl = "";
		
		try {
			videoUrl = this.saveFileToDir(file, VIDEO_DIR_NAME, ownerId);
		} catch (IOException e) {
			throw new VideoNotFoundException();
		}
		
		try {
			videoUrl = this.saveFileToDir(thumbnail, THUMBNAIL_DIR_NAME, ownerId);
		} catch (IOException e) {
			throw new VideoNotFoundException();
		}
		
		videoToSave.setUrl(videoUrl);
		videoToSave.setThumbnail(thumbnailUrl);
		
		this.saveFileToDB(videoToSave, channel);
	}
	
	public void saveFileToDB(VideoToSaveDTO videoToSave, Channel channel) {
		Video video = this.modelMapper.map(videoToSave, Video.class);
		
		video.setDateOfCreation(LocalDate.now());
		video.setOwner(channel);
		
		this.videosRepository.save(video);
	}
	
	//save file to directory and returns it's url
	private String saveFileToDir(MultipartFile file, String fileDir, Long ownerId) throws IOException, FileExistsException{
		byte[] bytes = file.getBytes();
		
		File userDir = new File(UPLOAD_DIR, fileDir + ownerId);
		userDir.mkdirs();
		
		File fileToSave = new File(userDir, file.getOriginalFilename());
		
		if(fileToSave.exists()) {
			throw new FileExistsException("File with name " + file.getOriginalFilename() + " exists!");
		}
		
		Path path = Paths.get(fileToSave.getPath());
		Files.write(path, bytes);
		
		return path.toString();
	}
	
	//TODO test this
//	@Transactional
//	private String saveFileToCloud(MultipartFile file, String fileDir, Long ownerId) throws IOException {
//		File tempFile = new File(file.getOriginalFilename());
//		@SuppressWarnings("rawtypes")
//		Map response = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("public_id", tempFile.getName()));
//		String url = (String) response.get("url");
//		return url;
//	}
//	
	private void checkFileFormat(MultipartFile file, String formats, String inputType) throws UnsupportedFileFormatException {
		String type = file.getContentType().toLowerCase();
		String[] supportedFormats = formats.split(" ");
		
		for(int format = 0; format < supportedFormats.length; format++) {
			if(type.contains(supportedFormats[format].toLowerCase())) {
				return;
			}
		}
		
		throw new UnsupportedFileFormatException(inputType + " not supported");
	}
	
}
