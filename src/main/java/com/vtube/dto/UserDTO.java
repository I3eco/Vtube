package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of user page view. Must be returned as JSON on GET -> /user/id request
 * @author I3eco
 *
 */
@Data
@NoArgsConstructor
public class UserDTO {
	@NonNull
	private Long id;
	
	@NonNull
	private String nickName;
	
	@NonNull
	private String email;
	
	private String profilePicture;
	private int numberOfOwnVideos;
	private int numberOfLikedVideos;	
}
