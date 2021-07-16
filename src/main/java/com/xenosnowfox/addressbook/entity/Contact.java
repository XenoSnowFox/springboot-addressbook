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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Getter
@NoArgsConstructor
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;

	@Column(nullable = false)
	@NotEmpty
	@Setter
	private String name;

	@ElementCollection
	private List<String> phoneNumbers;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
			joinColumns = {
					@JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false, updatable = false)
			}
			, inverseJoinColumns = {
					@JoinColumn(name = "addressbook_id", referencedColumnName = "id", nullable = false, updatable = false)
			}
	)
	private final Set<AddressBook> addressBooks = new HashSet<>();

	public Contact(final String withName, final List<String> withPhoneNumbers) {
		this.name = withName;
		this.phoneNumbers = List.copyOf(withPhoneNumbers);
	}

	@Override
	public boolean equals(final Object other) {
		if (!(other instanceof Contact)) {
			return false;
		}

		if (other == this) {
			return true;
		}

		Contact contact = (Contact) other;
		return contact.getId()
				.equals(this.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name);
	}
}
