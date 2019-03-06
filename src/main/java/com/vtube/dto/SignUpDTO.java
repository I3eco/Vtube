package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of sign up page view. Must be taken as JSON on POST -> /signup request
 * @author I3eco
 *
 */
@Data
@NoArgsConstructor
public class SignUpDTO {

	@NonNull
	private String nickName;
	
	private String profilePicture;
	private String firstName;
	private String lastName;

	@NonNull
	private String email;

	@NonNull
	private String password;

	private int age;
}
