/**
 * Contains model and persistence related classes - for storing data to database
 * and on disk.
 */
package com.lzadrija.persistence;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Signals that an exception occured while writing data to file. This class
 * is used as a wrapper for I/O exceptions.
 * Annotation {@link org.springframework.web.bind.annotation.ResponseStatus} is
 * used to mark this class with the HTTP status code:
 * {@link org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR} and
 * reason that should be returned.
 * 
 * @author lzadrija
 * 
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to persist validation results to disk")
public class StorageServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an StorageServiceException with null as its error detail
	 * message.
	 */
	public StorageServiceException() {
		super();
	}

	/**
	 * Constructs an StorageServiceException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            Throwable.getMessage() method)
	 */
	public StorageServiceException(String message) {
		super(message);
	}

	/**
	 * Constructs an StorageServiceException with the specified cause and a
	 * detail message of (cause==null ? null : cause.toString()) (which
	 * typically contains the class and detail message of cause).
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public StorageServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an StorageServiceException with the specified detail message
	 * and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            Throwable.getCause() method). (A null value is permitted, and
	 *            indicates that the cause is nonexistent or unknown.)
	 */
	public StorageServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
