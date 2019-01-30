package com.adacore.adaintellij.misc.cache;

import org.jetbrains.annotations.Nullable;

/**
 * Immutable data class representing wrapped cache data.
 * @see com.adacore.adaintellij.misc.cache.Cacher
 *
 * @param <T> The type of data that an instance of this
 *            class represents.
 */
final class CacheData<T> {

	/**
	 * Internal cached data.
	 */
	@Nullable
	public final T data;

	/**
	 * Constructs a new CacheData given some data.
	 *
	 * @param data The actual data to cache.
	 */
	CacheData(@Nullable T data) { this.data = data; }

}
