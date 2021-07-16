package com.xenosnowfox.addressbook.repository;

import com.xenosnowfox.addressbook.entity.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisting Contacts.
 */
@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
}
