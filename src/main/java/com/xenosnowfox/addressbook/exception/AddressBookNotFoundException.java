package com.xenosnowfox.addressbook.exception;

/**
 * Exception that is thrown when attempting to retrieve a non-existent address book from the system.
 */
public class AddressBookNotFoundException extends RuntimeException {

	/**
	 * Instantiates a new instance with a specified message.
	 *
	 * @param withMessage Error message.
	 */
	public AddressBookNotFoundException(final String withMessage) {
		super(withMessage);
	}

	/**
	 * Default constructor.
	 */
	public AddressBookNotFoundException() {
		super();
	}
}
