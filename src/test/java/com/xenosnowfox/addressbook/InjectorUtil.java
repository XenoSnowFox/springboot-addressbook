package com.xenosnowfox.addressbook;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
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
	 * @param withContactRepository
	 * 		Repository to insert the contact into.
	 * @return Randomly created contact.
	 */
	public Contact injectRandomContact(final ContactRepository withContactRepository) {
		final String name = generateRandomContactName();
		final List<String> phoneNumbers = generateRandomPhoneNumbers();
		return withContactRepository.save(new Contact(name, phoneNumbers));
	}

	public String generateRandomContactName() {
		return RandomStringUtils.randomAlphabetic(1).toUpperCase(Locale.ROOT)
				+ RandomStringUtils.randomAlphabetic(1, 25).toLowerCase(Locale.ROOT)
				+ " "
				+ RandomStringUtils.randomAlphabetic(1).toUpperCase(Locale.ROOT)
				+ RandomStringUtils.randomAlphabetic(1, 25).toLowerCase(Locale.ROOT);
	}

	public List<String> generateRandomPhoneNumbers() {
		return IntStream.generate(() -> ThreadLocalRandom.current()
				.nextInt(400000000, 500000000))
				.limit(ThreadLocalRandom.current()
						.nextInt(1, 10))
				.boxed()
				.map(i -> "0" + i)
				.collect(Collectors.toList());
	}
}
