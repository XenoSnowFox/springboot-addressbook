package com.xenosnowfox.addressbook.repository;

import com.xenosnowfox.addressbook.entity.AddressBook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisting Address Books.
 */
@Repository
public interface AddressBookRepository extends CrudRepository<AddressBook, Integer> {
}
