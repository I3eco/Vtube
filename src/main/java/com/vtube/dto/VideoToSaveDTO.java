package com.vtube.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of upload video object. 
 * Must be converted from POST -> /videos request
 * @author I3eco
 *
 */
@Data
@NoArgsConstructor
public class VideoToSaveDTO {
	
	@NonNull
	private String title;
	
	@NonNull
	private String url;
	
	@NonNull
	private String thumbnail;
	
	private String description;
}
