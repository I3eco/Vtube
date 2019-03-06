package com.vtube.security;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class for managing passwords encryption
 * @author I3eco
 *
 */
public class PasswordHandler{
	
	public static String encryptPassword(String password) {		
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		
		return encryptedPassword;
	}
	
	@ExceptionHandler
	public static boolean checkPasswordById(String password, String encryptedPassword) {
		return BCrypt.checkpw(password, encryptedPassword);
	}
}
