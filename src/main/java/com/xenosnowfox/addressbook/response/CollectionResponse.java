package com.xenosnowfox.addressbook.response;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.stream.StreamSupport;

/**
 * Response wrapper for returning a collection of items.
 *
 * @param <ENTITY> Data type of the response object.
 */
@Getter
@ToString
public class CollectionResponse<ENTITY> {

	/**
	 * Collection of items being returned from the request.
	 */
	Iterable<ENTITY> items;

	/**
	 * Instantiates a new instance.
	 *
	 * @param withItems Items to be returned.
	 */
	public CollectionResponse(@NonNull final Iterable<ENTITY> withItems) {
		this.items = withItems;
	}

	/**
	 * Returns the number of items contained in the response collection.
	 *
	 * @return Number of items.
	 */
	public long getCount() {
		return StreamSupport.stream(this.items.spliterator(), false)
				.count();
	}
}
