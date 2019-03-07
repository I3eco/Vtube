package com.vtube.UserValidation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid input for password!")
public class InvalidPasswordException extends Exception {

	public InvalidPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidPasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3859485686018424355L;

}
