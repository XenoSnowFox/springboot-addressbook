package com.xenosnowfox.addressbook.controller;

import com.xenosnowfox.addressbook.InjectorUtil;
import com.xenosnowfox.addressbook.entity.AddressBook;
import com.xenosnowfox.addressbook.entity.Contact;
import com.xenosnowfox.addressbook.repository.AddressBookRepository;
import com.xenosnowfox.addressbook.repository.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
public class ContactControllerIntegrationTest {

	@Autowired
	private ContactController contactController;

	@Autowired
	private AddressBookRepository addressBookRepository;

	@Autowired
	private ContactRepository contactRepository;

	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	@DisplayName("Ensure a contact can be completely removed")
	void testDeleteContact() {
		final Set<AddressBook> addressBookSet = InjectorUtil.injectRandomNumberOfAddressBooks(
				this.addressBookRepository);
		final Contact contact = InjectorUtil.injectRandomContact(
				this.contactRepository, addressBookSet.toArray(AddressBook[]::new));

		final ResponseEntity<Void> response = this.contactController.deleteContact(contact.getId());
		Assertions.assertEquals(204, response.getStatusCodeValue());

		Assertions.assertFalse(this.contactRepository.findById(contact.getId())
				.isPresent());
	}
}
