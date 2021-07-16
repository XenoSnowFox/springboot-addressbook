package com.xenosnowfox.addressbook.exception;

/**
 * Exception that is thrown when attempting to retrieve a non-existent contact from the system.
 */
public class ContactNotFoundException extends RuntimeException {

	/**
	 * Instantiates a new instance with a specified message.
	 *
	 * @param withMessage Error message.
	 */
	public ContactNotFoundException(final String withMessage) {
		super(withMessage);
	}

	/**
	 * Default constructor.
	 */
	public ContactNotFoundException() {
		super();
	}
}
