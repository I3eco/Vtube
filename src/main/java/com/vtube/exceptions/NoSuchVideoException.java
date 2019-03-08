package com.vtube.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such video!")
public class NoSuchVideoException extends Exception {

	public NoSuchVideoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NoSuchVideoException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public NoSuchVideoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public NoSuchVideoException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public NoSuchVideoException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1260028258347291842L;

}
