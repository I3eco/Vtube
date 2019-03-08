package com.vtube.dto;

import java.time.LocalDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is representation of video object in list of videos. 
 * Must be returned as a JSON on GET -> /watch?id=(something) request
 * @author I3eco
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class VideoDTO extends SmallVideoDTO {

	@NonNull
	private Long channelId;

	private String description;

	@NonNull
	private LocalDate dateOfCreation;
	
	public VideoDTO(@NonNull Long id, @NonNull String channelName, int numberOfViews, @NonNull String thumbnailUrl, 
			Long channelId, String description, LocalDate dateOfCreation) {
		super(id, channelName, numberOfViews, thumbnailUrl);
		this.channelId = channelId;
		this.description = description;
		this.dateOfCreation = dateOfCreation;
	}

}
