package com.xenosnowfox.addressbook.service;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.AddressBookContact;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AddressBookService {

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private ContactService contactService;

	/**
	 * Validates the provided address book.
	 *
	 * @param withAddressBook
	 * 		Address book to validate
	 * @throws IllegalStateException
	 * 		if the given address book does not contain a name.
	 */
	public void validate(final AddressBook withAddressBook) {
		if (withAddressBook.getName()
				.isBlank()) {
			throw new IllegalStateException("Address Book name cannot be blank.");
		}
	}

	/**
	 * Retrieves an Address Book by its identifier value.
	 *
	 * @param withIdentifier
	 * 		Identifier of the address book to retrieve.
	 * @return Optional describing an address book.
	 */
	public Optional<AddressBook> findById(final Integer withIdentifier) {
		return this.addressBookRepository.findById(withIdentifier);
	}

	/**
	 * Retrieves all instances of address book.
	 *
	 * @return Set of Address Books.
	 */
	public Set<AddressBook> findAll() {
		final Iterable<AddressBook> addressBooks = this.addressBookRepository.findAll();
		final Set<AddressBook> addressBookSet = new HashSet<>();
		for (final AddressBook addressBook : addressBooks) {
			addressBookSet.add(addressBook);
		}
		return addressBookSet;
	}

	/**
	 * Deletes the specified address book.
	 *
	 * @param withAddressBook
	 * 		Address book to be deleted.
	 */
	public void delete(final AddressBook withAddressBook) {
		Set<AddressBookContact> addressBookContacts = withAddressBook.getAddressBookContacts();

		// delete the address book itself
		this.addressBookRepository.delete(withAddressBook);

		// delete all orphaned contacts
		addressBookContacts.stream()
				.map(AddressBookContact::getContact)
				.filter(x -> x.getAddressBookContacts()
						.isEmpty())
				.forEach(this.contactService::delete);
	}

	/**
	 * Saves the specified address book.
	 *
	 * @param withAddressBook
	 * 		Address book to be saved.
	 * @return The updated address book
	 */
	public AddressBook save(final AddressBook withAddressBook) {
		this.validate(withAddressBook);
		return this.addressBookRepository.save(withAddressBook);
	}
}
