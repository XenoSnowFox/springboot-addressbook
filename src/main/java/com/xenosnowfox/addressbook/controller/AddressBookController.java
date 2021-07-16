package com.xenosnowfox.addressbook.controller;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.AddressBookContact;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.exception.AddressBookNotFoundException;
import com.xenosnowfox.addressbook.exception.ContactNotFoundException;
import com.xenosnowfox.addressbook.response.CollectionResponse;
import com.xenosnowfox.addressbook.service.AddressBookService;
import com.xenosnowfox.addressbook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Rest endpoints for managing an address book.
 */
@RestController
@RequestMapping("/addressbooks")
public class AddressBookController {

	@Autowired
	private AddressBookService addressBookService;

	@Autowired
	private ContactService contactService;

	/**
	 * API endpoint that returns a collection of address books.
	 *
	 * @return Collection response containing address books.
	 */
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			method = "GET"
			, tags = {"Address Book"}
			, summary = "List all Address Books"
			, description = "Retrieves a collection of all the address books."
			, operationId = "getAllAddressBooks"
	)
	public CollectionResponse<AddressBook> getAllAddressBooks() {
		return new CollectionResponse<>(this.addressBookService.findAll());
	}

	/**
	 * Rest API endpoint for retrieving an Address book with the specified identifier.
	 *
	 * @param withId
	 * 		Identifier of the address to retrieve contacts from.
	 * @return Address book
	 * @throws AddressBookNotFoundException
	 * 		if no address book, with the specified identifier, cannot be found
	 */
	@GetMapping(value = "/{id}")
	@Operation(
			method = "GET"
			, tags = {"Address Book"}
			, summary = "Get an Address Book"
			, description = "Retrieves the details about a single address book."
			, operationId = "getAddressBook"
	)
	public AddressBook getAddressBook(@PathVariable("id") final Integer withId) throws AddressBookNotFoundException {
		return this.addressBookService.findById(withId)
				.orElseThrow(AddressBookNotFoundException::new);
	}

	/**
	 * Rest API endpoint for deleting an entire address book, including any contacts that were only part of this
	 * address
	 * book.
	 *
	 * @param withId
	 * 		Identifier of the address book to delete
	 * @return No Content Response Entity
	 * @throws AddressBookNotFoundException
	 * 		if no address book, with the specified identifier, can be found
	 */
	@DeleteMapping(value = "/{id}")
	@Operation(
			method = "DELETE"
			, tags = {"Address Book"}
			, summary = "Delete an Address Book"
			, description = "Deletes and entire Address Book, removing any orphaned contacts in the process."
			, operationId = "deleteAddressBook"
	)
	public ResponseEntity<Void> deleteAddressBook(@PathVariable("id") Integer withId)
			throws AddressBookNotFoundException {
		final AddressBook addressBook = this.getAddressBook(withId);
		this.addressBookService.delete(addressBook);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Rest API endpoint for creating a new address book entry.
	 *
	 * @param withAddressBook
	 * 		Details of the newly created endpoint
	 * @return The newly created address book entry.
	 */
	@PostMapping
	@Operation(
			method = "POST"
			, tags = {"Address Book"}
			, summary = "Create a new Address Book"
			, description = "Creates a new Address Book instance."
			, operationId = "createAddressBook"
	)
	public AddressBook createAddressBook(@Valid @RequestBody AddressBook withAddressBook) {
		return this.addressBookService.save(withAddressBook);
	}

	/**
	 * Rest API endpoint for retrieving a collection of Contacts from an Address book with the specified identifier.
	 *
	 * @param withId
	 * 		Identifier of the address book to retrieve contacts from.
	 * @return Collection Response containing the address book's contacts
	 * @throws AddressBookNotFoundException
	 * 		if no address book, with the specified identifier, can be found
	 */
	@GetMapping(value = "/{id}/contacts")
	@Operation(
			method = "GET"
			, tags = {"Address Book"}
			, summary = "Get all Contact for an Address Book"
			, description = "Returns all the contacts for a specific address book."
			, operationId = "getContactsFromAddressBook"
	)
	public CollectionResponse<Contact> getContactsFromAddressBook(@PathVariable("id") Integer withId)
			throws AddressBookNotFoundException {
		Collection<Contact> contacts = this.getAddressBook(withId)
				.getAddressBookContacts()
				.stream()
				.map(AddressBookContact::getContact)
				.collect(Collectors.toSet());
		return new CollectionResponse<>(contacts);
	}

	/**
	 * Rest API endpoint for removing an individual contact from an Address book, deleting the contact entirely if
	 * it no
	 * longer belongs to any other address books.
	 *
	 * @param withAddressId
	 * 		Identifier of the address book to remove a contact from.
	 * @param withContactId
	 * 		Identifier of the contact to remove from the address book
	 * @return No Content Response Entity.
	 * @throws AddressBookNotFoundException
	 * 		if no address book, with the specified identifier, can be found
	 * @throws ContactNotFoundException
	 * 		if the contact, with the specified identifier, cannot be found or isn't part of the requested address book
	 */
	@DeleteMapping(value = "/{addressbook_id}/contacts/{contact_id}")
	@Operation(
			method = "DELETE"
			, tags = {"Address Book"}
			, summary = "Remove a Contact from an Address Book"
			,
			description = "Removes the specific Contact from an Address Book. The Contact will be deleted in it's "
					+ "entirety if it isn't linked to any other address book."
			, operationId = "deleteContactFromAddressBook"
	)
	public ResponseEntity<Void> deleteContactFromAddressBook(
			@PathVariable("addressbook_id") final Integer withAddressId
			, @PathVariable("contact_id") final Integer withContactId
	) throws AddressBookNotFoundException, ContactNotFoundException {
		AddressBook addressBook = this.getAddressBook(withAddressId);
		AddressBookContact addressBookContact = addressBook.getAddressBookContacts()
				.stream()
				.filter(item -> item.getContact()
						.getId()
						.equals(withContactId))
				.findAny()
				.orElseThrow(ContactNotFoundException::new);

		addressBook.getAddressBookContacts()
				.remove(addressBookContact);
		this.addressBookService.save(addressBook);

		// check if the contact needs to be deleted
		Contact contact = addressBookContact.getContact();
		if (contact.getAddressBookContacts()
				.size() == 0) {
			this.contactService.delete(contact);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Rest API endpoint to create a new Contact entry within a given address book.
	 *
	 * @param withAddressId
	 * 		Identifier of the address book to add the contact to.
	 * @param withContact
	 * 		Details of the contact to create.
	 * @return Newly created contact.
	 * @throws AddressBookNotFoundException
	 * 		if the specified address book does not exist.
	 */
	@PostMapping(value = "/{id}/contacts")
	@Operation(
			method = "POST"
			, tags = {"Address Book"}
			, summary = "Creates a new Contact in an Address Book"
			, description = "Creates a new contact instance, assigning it to the specified Address Book."
			, operationId = "createNewContactInAddressBook"
	)
	public Contact createNewContactInAddressBook(
			@PathVariable("id") final Integer withAddressId
			, @RequestBody final Contact withContact
	) throws AddressBookNotFoundException {
		AddressBook addressBook = this.getAddressBook(withAddressId);
		withContact.addAddressBooks(addressBook);
		return this.contactService.save(withContact);
	}
}
