package com.xenosnowfox.addressbook.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AddressBookContact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "addressbook_id")
	private AddressBook addressBook;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "contact_id")
	private Contact contact;

	public AddressBookContact(final AddressBook withAddressBook) {
		this.addressBook = withAddressBook;
	}

	public AddressBookContact(final Contact withContact) {
		this.contact = withContact;
	}

	public AddressBookContact(final AddressBook withAddressBook, final Contact withContact) {
		this.addressBook = withAddressBook;
		this.contact = withContact;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof AddressBookContact)) {
			return false;
		}

		AddressBookContact other = (AddressBookContact) o;
		return Objects.equals(this.addressBook, other.getAddressBook())
				&& Objects.equals(this.contact, other.getContact());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.addressBook.hashCode(), this.contact.hashCode());
	}
}
