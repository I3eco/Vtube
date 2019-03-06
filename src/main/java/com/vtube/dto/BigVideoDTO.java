package com.vtube.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This is representation of main watch video page view. Must be returned as JSON on GET -> /video/id request
 * @author I3eco
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class BigVideoDTO extends VideoDTO {

	@NonNull
	private String url;
	
	int likes;
	int dislikes;
	List<CommentDTO> comments;
	
	@NonNull
	List<SmallVideoDTO> offeredVideos;
	
	public BigVideoDTO(@NonNull Long id, @NonNull String channelName, int numberOfViews, @NonNull String thumbnailUrl,
			Long channelId, String description, LocalDate dateOfCreation,
			String url, int likes, int dislikes, List<CommentDTO> comments, List<SmallVideoDTO> offeredVideos) {
		super(id, channelName, numberOfViews, thumbnailUrl, channelId, description, dateOfCreation);
		this.url = url;
		this.likes = likes;
		this.dislikes = dislikes;
		this.comments = comments;
		this.offeredVideos = offeredVideos;
	}
}
