/**
 * 
 */
package com.lzadrija.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author lzadrija
 * 
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to persist validation results to disk")
public class StorageServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public StorageServiceException() {
		super();
	}

	/**
	 * 
	 * @param msg
	 */
	public StorageServiceException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param cause
	 */
	public StorageServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public StorageServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
