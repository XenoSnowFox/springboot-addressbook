package com.xenosnowfox.addressbook.controller;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.exception.ContactNotFoundException;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import com.xenosnowfox.addressbook.response.CollectionResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest endpoints for managing contacts.
 */
@RestController
@RequestMapping("/contacts")
public class ContactController {

	@Autowired
	private ContactRepository contactRepository;

	/**
	 * API endpoint that returns a collection of contact from across all address books.
	 *
	 * @return Collection response containing contacts.
	 */
	@GetMapping()
	@Operation(
			method = "GET"
			, tags = {"Contact"}
			, summary = "List all Contacts"
			, description = "Returns a list off all Contacts across all Address Books"
			, operationId = "getAllContacts"
	)
	public CollectionResponse<Contact> getAllContacts() {
		return new CollectionResponse<>(this.contactRepository.findAll());
	}

	/**
	 * Rest API endpoint for retrieving a Contact with the specified identifier.
	 *
	 * @param withId
	 * 		Identifier of the contact to retrieve
	 * @return Contact
	 * @throws ContactNotFoundException
	 * 		if the contact, with the specified identifier, cannot be found
	 */
	@GetMapping(value = "/{id}")
	@Operation(
			method = "GET"
			, tags = {"Contact"}
			, summary = "Get a Contact"
			, description = "Returns details of the specified Contact"
			, operationId = "getContact"
	)
	public Contact getContact(@PathVariable("id") final Integer withId) throws ContactNotFoundException {
		return this.contactRepository.findById(withId)
				.orElseThrow(ContactNotFoundException::new);
	}

	/**
	 * Rest API endpoint for retrieving a collection off address books containing the specified contact.
	 *
	 * @param withId
	 * 		Identifier of the contact.
	 * @return Collection of address books.
	 * @throws ContactNotFoundException
	 * 		if the specified contact does not exist.
	 */
	@GetMapping(value = "/{id}/addressbooks")
	@Operation(
			method = "GET"
			, tags = {"Contact"}
			, summary = "Get all Address Books containing a Contact"
			, description = "Returns the collection of Address Books that the specified Client is part of."
			, operationId = "getClientAddressBooks"
	)
	public CollectionResponse<AddressBook> getClientAddressBooks(@PathVariable("id") final Integer withId)
			throws ContactNotFoundException {
		Contact contact = this.getContact(withId);
		return new CollectionResponse<>(contact.getAddressBooks());
	}

	/**
	 * Rest API endpoint for deleting a contact, removing it from all linked address books.
	 *
	 * @param withId
	 * 		Identifier of the contact to delete.
	 * @return No Content Response Entity
	 * @throws ContactNotFoundException
	 * 		if the contact, with the specified identifier, cannot be found
	 */
	@DeleteMapping(value = "/{id}")
	@Operation(
			method = "DELETE"
			, tags = {"Contact"}
			, summary = "Delete a Contact"
			, description = "Removes the specified Contact from the system, removing it from all linked Address Books."
			, operationId = "deleteContact"
	)
	public ResponseEntity<Void> deleteContact(@PathVariable("id") Integer withId) throws ContactNotFoundException {
		Contact contact = this.getContact(withId);
		this.contactRepository.delete(contact);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Rest API endpoint for updating a contact's details.
	 *
	 * @param withId
	 * 		Identifier of the contact to update.
	 * @param withContact
	 * 		Details to update the contact with.
	 * @return Updated contact details
	 * @throws ContactNotFoundException
	 * 		if the contact, with the specified identifier, cannot be found
	 */
	@PutMapping(value = "/{id}")
	@Operation(
			method = "PUT"
			, tags = {"Contact"}
			, summary = "Update a Contact"
			,
			description = "Updates the information associated to the specified contact, replacing all it's data with "
					+ "that provided."
			, operationId = "updateContact"
	)
	public Contact updateContact(@PathVariable("id") final Integer withId, @RequestBody final Contact withContact) {
		Contact contact = this.getContact(withId);
		contact.setName(withContact.getName());
		contact.getPhoneNumbers()
				.clear();
		contact.getPhoneNumbers()
				.addAll(withContact.getPhoneNumbers());
		this.contactRepository.save(contact);
		return contact;
	}
}
