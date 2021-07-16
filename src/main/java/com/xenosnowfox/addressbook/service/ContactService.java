package com.xenosnowfox.addressbook.service;

import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	/**
	 * Validates the provided contact.
	 *
	 * @param withContact
	 * 		Contact to validate
	 * @throws IllegalStateException
	 * 		if the given contact does not contain a name or isn't assigned to at least one address book.
	 */
	public void validate(final Contact withContact) {
		if (withContact.getName()
				.isBlank()) {
			throw new IllegalStateException("Contact name cannot be blank.");
		}

		if (withContact.getAddressBookContacts()
				.size() == 0) {
			throw new IllegalStateException("Contact must belong to at least one address book.");
		}
	}

	/**
	 * Retrieves a Contact by its identifier value.
	 *
	 * @param withIdentifier
	 * 		Identifier of the contact to retrieve.
	 * @return Optional describing a contact.
	 */
	public Optional<Contact> findById(final Integer withIdentifier) {
		return this.contactRepository.findById(withIdentifier);
	}

	/**
	 * Retrieves all instances of contact.
	 *
	 * @return Set of Contacts.
	 */
	public Set<Contact> findAll() {
		final Iterable<Contact> contacts = this.contactRepository.findAll();
		final Set<Contact> contactSet = new HashSet<>();
		for (Contact contact : contacts) {
			contactSet.add(contact);
		}
		return contactSet;
	}

	/**
	 * Deletes the specified contact.
	 *
	 * @param withContact
	 * 		Contact to be deleted.
	 */
	public void delete(final Contact withContact) {
		this.contactRepository.delete(withContact);
	}

	/**
	 * Saves the specified contact.
	 *
	 * @param withContact
	 * 		Contact to be saved.
	 * @return The updated contact
	 */
	public Contact save(final Contact withContact) {
		this.validate(withContact);
		return this.contactRepository.save(withContact);
	}
}
