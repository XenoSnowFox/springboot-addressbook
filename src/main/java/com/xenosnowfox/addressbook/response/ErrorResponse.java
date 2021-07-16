package com.xenosnowfox.addressbook.response;

import lombok.Getter;
import lombok.NonNull;

/**
 * Response wrapper for returning a standardize error message.
 */
@Getter
public class ErrorResponse {

	/**
	 * Status code.
	 */
	private final int status;

	/**
	 * Error message.
	 */
	private final String error;

	/**
	 * Instantiates a new instance.
	 *
	 * @param withStatusCode
	 * 		HTTP status code
	 * @param withErrorMessage
	 * 		Human readable error message.
	 */
	public ErrorResponse(final int withStatusCode, @NonNull final String withErrorMessage) {
		this.status = withStatusCode;
		this.error = withErrorMessage;
	}
}
