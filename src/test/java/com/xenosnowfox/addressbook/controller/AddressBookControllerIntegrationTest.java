package com.xenosnowfox.addressbook.controller;

import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.exception.AddressBookNotFoundException;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import com.xenosnowfox.addressbook.response.CollectionResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class AddressBookControllerIntegrationTest {

	private static SessionFactory sessionFactory;

	@Autowired
	private AddressBookController addressBookController;

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that getAddressBook returns the correct data")
	public void testGetAddressBookSuccessfullyReturnsTheCorrectAddressBook() {
		// Set up our control data
		final AddressBook expected = InjectorUtil.injectRandomAddressBook(this.addressBookRepository);

		// Run the test
		AddressBook returnedAddressBook = this.addressBookController.getAddressBook(expected.getId());

		// Assert the results
		Assertions.assertNotNull(returnedAddressBook);
		Assertions.assertEquals(expected.getId(), returnedAddressBook.getId());
		Assertions.assertEquals(expected.getName(), returnedAddressBook.getName());
	}

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that getAddressBook throws an exception if the requested address book does not exist")
	public void testGetAddressBookThrowsExceptionIdBookDoesNotExist() {
		// Set up our control data
		final int addressBookIdentifier = ThreadLocalRandom.current()
				.nextInt();

		// Run the test
		Assertions.assertThrows(
				AddressBookNotFoundException.class
				, () -> this.addressBookController.getAddressBook(addressBookIdentifier)
		);
	}

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that getAddressBooks returns all data")
	public void testGetAddressBookSuccessfullyReturnsTheCorrectData() {
		// Set up our control data
		Set<AddressBook> addressBooks = InjectorUtil.injectRandomNumberOfAddressBooks(this.addressBookRepository);
		Assertions.assertNotEquals(0, addressBooks.size());

		// Run the test
		CollectionResponse<AddressBook> response = this.addressBookController.getAllAddressBooks();

		// Assert the results
		Assertions.assertEquals(addressBooks.size(), response.getCount());
		for (AddressBook actual : response.getItems()) {
			Assertions.assertTrue(addressBooks.contains(actual));
		}
	}

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that createAddressBook correctly inserts a new address book")
	public void testCreateAddressBookCorrectlyInsertsNewAddressBook() {

		// set up our control data
		AddressBook expected = new AddressBook(RandomStringUtils.randomAlphanumeric(50));

		// Run the test
		AddressBook actual = this.addressBookController.createAddressBook(expected);

		// Assert the results
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(expected.getName(), actual.getName());

		// Assert directly from repository
		Optional<AddressBook> opt = this.addressBookRepository.findById(actual.getId());
		Assertions.assertTrue(opt.isPresent());
		Assertions.assertEquals(expected.getName(), opt.get()
				.getName());
	}

	@Test
	@DisplayName("Ensure that deleteAddressBook correctly remove an empty address book")
	public void testDeleteAddressBookCorrectlyRemoveAnEmptyAddressBook() {
		// set up our control data
		AddressBook expected = InjectorUtil.injectRandomAddressBook(this.addressBookRepository);

		// Run the test
		this.addressBookController.deleteAddressBook(expected.getId());

		// Assert the address book was removed from the repository
		Optional<AddressBook> opt = this.addressBookRepository.findById(expected.getId());
		Assertions.assertTrue(opt.isEmpty());

		// Assert the the address book was removed from the full list
		Iterable<AddressBook> collection = this.addressBookRepository.findAll();
		for (AddressBook item : collection) {
			Assertions.assertNotEquals(expected, item);
		}
	}

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that deleteAddressBook only removes the requested address book")
	public void testDeleteAddressBookOnlyRemovesRequestedAddressBook() {
		// set up the control data
		List<AddressBook> expected = new ArrayList<>(
				InjectorUtil.injectRandomNumberOfAddressBooks(this.addressBookRepository));
		Collections.shuffle(expected);
		AddressBook target = expected.remove(0);

		// Run the test
		this.addressBookController.deleteAddressBook(target.getId());

		// Assert the address book was removed from the repository
		Optional<AddressBook> opt = this.addressBookRepository.findById(target.getId());
		Assertions.assertTrue(opt.isEmpty());

		// Assert the the address book was removed from the full list
		Iterable<AddressBook> collection = this.addressBookRepository.findAll();

		for (AddressBook item : collection) {
			System.out.println(item);
			Assertions.assertNotEquals(target, item);
		}
		Assertions.assertEquals(expected.size(), StreamSupport.stream(collection.spliterator(), false)
				.count());

		// Assert that the remaining items remained in the repository
		for (AddressBook item : collection) {
			Assertions.assertTrue(expected.contains(item));
		}
	}
}
