package com.xenosnowfox.addressbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class AddressBook {

	/**
	 * Identifier of the address book.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;

	/**
	 * Human readable name representing the address book.
	 */
	@Setter
	@Column(nullable = false)
	@NotEmpty
	private String name;

	/**
	 * Collection of contacts that are listed within the address book.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "addressBook", cascade = CascadeType.ALL)
	private Set<AddressBookContact> addressBookContacts;

	/**
	 * Instantiates a new instance.
	 *
	 * @param withName
	 * 		Name of the new address book.
	 * @param contacts
	 *      Array of contacts to associate with this address book.
	 */
	public AddressBook(final String withName, Contact... contacts) {
		this.name = withName;
		this.addressBookContacts = Stream.of(contacts)
				.map(contact -> new AddressBookContact(this, contact))
				.collect(Collectors.toSet());
	}

}
