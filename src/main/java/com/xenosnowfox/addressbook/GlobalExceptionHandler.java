package com.xenosnowfox.addressbook;

import com.xenosnowfox.addressbook.exception.AddressBookNotFoundException;
import com.xenosnowfox.addressbook.exception.ContactNotFoundException;
import com.xenosnowfox.addressbook.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AddressBookNotFoundException.class)
	public ResponseEntity<ErrorResponse> addressBookNotFoundException(
			final AddressBookNotFoundException withException
	) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Address Book not found.");
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ContactNotFoundException.class)
	public ResponseEntity<ErrorResponse> contactNotFoundException(
			final ContactNotFoundException withException
	) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Contact not found.");
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
}
