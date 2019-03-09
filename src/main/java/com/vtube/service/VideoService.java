package com.vtube.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
//import java.util.Map;
import java.util.NoSuchElementException;

//import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vtube.dal.UsersRepository;
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
import com.vtube.dal.VideosRepository;
import com.vtube.dto.BigVideoDTO;
import com.vtube.dto.CreatedVideoDTO;
import com.vtube.dto.SmallVideoDTO;
import com.vtube.dto.VideoDTO;
import com.vtube.dto.VideoToSaveDTO;
import com.vtube.exceptions.FileExistsException;
import com.vtube.exceptions.UnsupportedFileFormatException;
import com.vtube.exceptions.VideoNotFoundException;
import com.vtube.model.Channel;
import com.vtube.model.User;
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
	private static final String UPLOAD_DIR = "..\\VTubeFileStorage\\";

//	private static final Cloudinary cloudinary = new Cloudinary
//			("CLOUDINARY_URL=cloudinary://689344796343136:jpv4nWOWuPXqg-fYL0NFfxpAxVE@vtubeto");

	@Autowired
	private VideosRepository videosRepository;
	
	@Autowired
	private UsersRepository usersRepository;

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

	public CreatedVideoDTO uploadVideoData(MultipartFile file, MultipartFile thumbnail, String title,
			String description, Long ownerId, Channel channel)
			throws FileExistsException, VideoNotFoundException, UnsupportedFileFormatException {
		this.checkFileFormat(file, SUPPORTED_VIDEO_FORMATS, "Video");
		this.checkFileFormat(thumbnail, SUPPORTED_THUMBNAIL_FORMATS, "Picture");

		VideoToSaveDTO videoToSave = new VideoToSaveDTO();
		videoToSave.setDescription(description);
		if (title == null || title.trim().equals("")) {
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
			thumbnailUrl = this.saveFileToDir(thumbnail, THUMBNAIL_DIR_NAME, ownerId);
		} catch (IOException e) {
			throw new VideoNotFoundException();
		}

		videoToSave.setUrl(videoUrl);
		videoToSave.setThumbnail(thumbnailUrl);

		Long videoId = this.saveFileToDB(videoToSave, channel);

		CreatedVideoDTO video = new CreatedVideoDTO();
		video.setId(videoId);
		video.setTitle(videoToSave.getTitle());
		video.setDescription(videoToSave.getDescription());
		video.setUrl(videoToSave.getUrl());

		return video;
	}

	public Long saveFileToDB(VideoToSaveDTO videoToSave, Channel channel) {
		Video video = this.modelMapper.map(videoToSave, Video.class);

		video.setDateOfCreation(LocalDate.now());
		video.setOwner(channel);

		Long id = this.videosRepository.save(video).getId();

		return id;
	}

	// save file to directory and returns it's url
	private String saveFileToDir(MultipartFile file, String fileDir, Long ownerId)
			throws IOException, FileExistsException {
		byte[] bytes = file.getBytes();

		File userDir = new File(UPLOAD_DIR, fileDir + ownerId);
		userDir.mkdirs();

		File fileToSave = new File(userDir, file.getOriginalFilename());

		if (fileToSave.exists()) {
			throw new FileExistsException("File with name " + file.getOriginalFilename() + " exists!");
		}

		Path path = Paths.get(fileToSave.getPath());
		Files.write(path, bytes);

		return path.toString();
	}

	// TODO test this
//	@Transactional
//	private String saveFileToCloud(MultipartFile file, String fileDir, Long ownerId) throws IOException {
//		File tempFile = new File(file.getOriginalFilename());
//		@SuppressWarnings("rawtypes")
//		Map response = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap("public_id", tempFile.getName()));
//		String url = (String) response.get("url");
//		return url;
//	}
//	
	private void checkFileFormat(MultipartFile file, String formats, String inputType)
			throws UnsupportedFileFormatException {
		String type = file.getContentType().toLowerCase();
		String[] supportedFormats = formats.split(" ");

		for (int format = 0; format < supportedFormats.length; format++) {
			if (type.contains(supportedFormats[format].toLowerCase())) {
				return;
			}
		}

		throw new UnsupportedFileFormatException(inputType + " not supported");
	}

	public BigVideoDTO getBigVideoDTOById(Long id) throws VideoNotFoundException {
		Video video = null;
		try {
			video = this.videosRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new VideoNotFoundException();
		}
		video.setNumberOfViews(video.getNumberOfViews() + 1);
		this.videosRepository.save(video);
		
		BigVideoDTO bigVideoDTO = this.convertFromVideoToBigVideoDTO(video);
		
		return bigVideoDTO;
	}
	
	public BigVideoDTO getBigVideoDTOById(Long id, Long userId) throws VideoNotFoundException {
		BigVideoDTO bigVideoDTO = this.getBigVideoDTOById(id);
		
		User user = this.usersRepository.findById(userId).get();
		Video video = this.videosRepository.findById(id).get();
		
		if(!user.getWatchedVideos().contains(video)) {
			user.getWatchedVideos().add(video);
			this.usersRepository.save(user);
		} else {
			video.setNumberOfViews(video.getNumberOfViews() - 1);
			this.videosRepository.save(video);
		}
		
		return bigVideoDTO;
	}
	
	public SmallVideoDTO convertFromVideoToSmallVideoDTO(Video parent) {
		SmallVideoDTO video = this.modelMapper.map(parent, SmallVideoDTO.class);

		video.setChannelName(parent.getOwner().getName());
		
		return video;
	}
	
	public VideoDTO convertFromVideoToVideoDTO(Video parent) {
		VideoDTO video = this.modelMapper.map(parent, VideoDTO.class);
		
		video.setChannelName(parent.getOwner().getName());
		video.setChannelId(parent.getOwner().getId());
		
		return video;
	}
	
	public BigVideoDTO convertFromVideoToBigVideoDTO(Video parent) {
		BigVideoDTO video = this.modelMapper.map(parent, BigVideoDTO.class);
		
		video.setChannelName(parent.getOwner().getName());
		video.setChannelId(parent.getOwner().getId());
		video.setLikes(parent.getUsersWhoLikeThisVideo().size());
		
		return video;
	}

//	public List<Video> findAllBySearchString(String search) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	

}
