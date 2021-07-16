package com.xenosnowfox.addressbook.controller;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class InjectorUtil {

	/**
	 * Helper method that injects a randomly generated address book into the repository.
	 *
	 * @return Randomly generated address book.
	 */
	public AddressBook injectRandomAddressBook(final AddressBookRepository withRepository) {
		final String title = RandomStringUtils.randomAlphanumeric(1, 100);
		final AddressBook addressBook = new AddressBook(title);
		return withRepository.save(addressBook);
	}

	/**
	 * Helper method that injects a random number of randomly generated address books into the repository.
	 *
	 * @return Set of randomly generated address books.
	 */
	public Set<AddressBook> injectRandomNumberOfAddressBooks(final AddressBookRepository withRepository) {
		final Set<AddressBook> addressBooks = new HashSet<>();
		final int total = ThreadLocalRandom.current()
				.nextInt(1, 10);
		for (int i = 0; i < total; i++) {
			addressBooks.add(injectRandomAddressBook(withRepository));
		}
		return addressBooks;
	}

	/**
	 * Helper method that injects a randomly generated contact into the provided address books.
	 *
	 * @param withAddressBookRepository
	 * 		Repository to insert the address into.
	 * @param withContactRepository
	 * 		Repository to insert the contact into.
	 * @param withAddressBooks
	 * 		Address Books to add the contact new
	 * @return Randomly created contact.
	 */
	public Contact injectRandomContact(final AddressBookRepository withAddressBookRepository, final ContactRepository withContactRepository, final AddressBook... withAddressBooks) {
		final String name = RandomStringUtils.randomAlphanumeric(1, 100);
		final List<String> phoneNumbers = IntStream.generate(() -> ThreadLocalRandom.current()
				.nextInt(400000000, 500000000))
				.limit(ThreadLocalRandom.current()
						.nextInt(1, 10))
				.boxed()
				.map(i -> "0" + i)
				.collect(Collectors.toList());

		final Contact contact = withContactRepository.save(new Contact(name, phoneNumbers));
		List.of(withAddressBooks)
				.forEach(contact.getAddressBooks()::add);
		withContactRepository.save(contact);
		return contact;
	}
}
