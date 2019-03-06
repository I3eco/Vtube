package com.vtube.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of comment object for main watch video page view. 
 * Must be returned as part of a JSON on GET -> /video/id request
 * @author I3eco
 *
 */
@Data
@NoArgsConstructor
public class CommentDTO {
	@NonNull
	private Long id;
	
	@NonNull
	private String content;
	
	private int likes;
	private int dislikes;
	List<CommentDTO> subComments;
}
