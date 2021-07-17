package com.xenosnowfox.addressbook.service;

import com.xenosnowfox.addressbook.InjectorUtil;
import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class   ContactServiceIntegrationTest {

	@Autowired
	private ContactService contactService;

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Test
	@Transactional(propagation= Propagation.REQUIRED)
	@DisplayName("Ensure that a new contact can be successfully created.")
	public void testSaveWithNewContact() {
		AddressBook addressBook = InjectorUtil.injectRandomAddressBook(this.addressBookRepository);
		Contact contact = new Contact(
				InjectorUtil.generateRandomContactName()
				, InjectorUtil.generateRandomPhoneNumbers()
				, addressBook
		);

		Contact actual = this.contactService.save(contact);

		Assertions.assertNotNull(actual);
		Assertions.assertEquals(contact.getName(), actual.getName());
		Assertions.assertEquals(new HashSet<>(contact.getPhoneNumbers()), new HashSet<>(actual.getPhoneNumbers()));

		Optional<Contact> check = this.contactRepository.findById(actual.getId());

		Assertions.assertTrue(check.isPresent());
		Assertions.assertEquals(contact.getName(), check.get().getName());
		Assertions.assertEquals(new HashSet<>(contact.getPhoneNumbers()), new HashSet<>(check.get().getPhoneNumbers()));
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	@DisplayName("Ensure a new Contact cannot be created with a blank name.")
	public void testSaveNewContactWithInvalidName() {
		AddressBook addressBook = InjectorUtil.injectRandomAddressBook(this.addressBookRepository);
		Contact contact = new Contact(
				""
				, Collections.emptyList()
				, addressBook
		);

		Assertions.assertThrows(
				IllegalStateException.class
				, () -> this.contactService.save(contact)
		);
	}

	@Test
	@Disabled
	@Transactional(propagation = Propagation.REQUIRED)
	@DisplayName("Ensure an existing Contact cannot be updated with a blank name.")
	public void testSaveUpdateContactWithInvalidName() {
		AddressBook addressBook = InjectorUtil.injectRandomAddressBook(this.addressBookRepository);
		Contact contact = InjectorUtil.injectRandomContact(this.contactRepository, addressBook);

		final String expectedName = contact.getName();

		contact.setName("");

		Assertions.assertThrows(
				IllegalStateException.class
				, () -> this.contactService.save(contact)
		);

		// assert that the persisted contact didn't change
		final Optional<Contact> actualContact = this.contactRepository.findById(contact.getId());
		Assertions.assertTrue(actualContact.isPresent());
		Assertions.assertEquals(expectedName, actualContact.get().getName());
	}
}
