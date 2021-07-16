package com.xenosnowfox.addressbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@ToString
@Entity
@Getter
@NoArgsConstructor
public class Contact {

	/**
	 * Identifier of the contact.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;

	/**
	 * Contact person's name.
	 */
	@Column(nullable = false)
	@NotEmpty
	@Setter
	private String name;

	/**
	 * Phone numbers associated with this contact.
	 */
	@ElementCollection
	private List<String> phoneNumbers;

	/**
	 * Collection of address books that this contact is part of.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
	private final Set<AddressBookContact> addressBookContacts = new HashSet<>();

	/**
	 * Instantiates a new instance.
	 *
	 * @param withName
	 * 		Contact's name
	 * @param withPhoneNumbers
	 * 		List of phone numbers
	 * @param withAddressBooks
	 * 		Array of address books to add this contact to.
	 */
	public Contact(final String withName, final List<String> withPhoneNumbers, final AddressBook... withAddressBooks) {
		this.name = withName;
		this.phoneNumbers = List.copyOf(withPhoneNumbers);
		this.addAddressBooks(withAddressBooks);
	}

	/**
	 * Adds the contact to the specified address books.
	 *
	 * @param withAddressBooks
	 * 		Array of address books.
	 */
	@JsonIgnore
	public void addAddressBooks(final AddressBook... withAddressBooks) {
		Stream.of(withAddressBooks)
				.map(addressBook -> new AddressBookContact(addressBook, this))
				.forEach(this.addressBookContacts::add);
	}
}
