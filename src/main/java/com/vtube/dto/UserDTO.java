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
public class UserDTO implements Idto{
	@NonNull
	private Long id;
	
	@NonNull
	private String nickName;
	
	private Long numberOfSubscribers;
	
	private String profilePicture;
	private int numberOfOwnVideos;
	private int numberOfLikedVideos;
}
