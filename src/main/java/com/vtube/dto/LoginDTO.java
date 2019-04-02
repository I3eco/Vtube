package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This is representation of login DTO object. 
 * Must be taken as  a JSON on GET -> /login
 * @author I3eco
 *
 */
@Data
@NoArgsConstructor
public class LoginDTO implements IDTO{
	@NonNull
	private String email;
	
	@NonNull
	private String password;
}
