package com.xenosnowfox.addressbook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Getter
@NoArgsConstructor
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
	@ManyToMany(mappedBy = "addressBooks", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private final Set<Contact> contacts = new HashSet<>();

	/**
	 * Instantiates a new instance.
	 *
	 * @param withName Name of the new address book.
	 */
	public AddressBook(final String withName) {
		this.name = withName;
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof AddressBook)) {
			return false;
		}

		if (other == this) {
			return true;
		}

		AddressBook addressBook = (AddressBook) other;
		return addressBook.getId()
				.equals(this.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name);
	}
}
