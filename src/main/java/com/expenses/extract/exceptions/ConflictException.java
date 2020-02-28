package com.expenses.extract.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ConflictException extends Exception{
	private static final long serialVersionUID = 1L;

	public ConflictException(String msg) {
		super(msg);
	}
}
