package com.adacore.adaintellij.misc.cache;

import org.jetbrains.annotations.Nullable;

/**
 * Immutable data class representing the result of a
 * cache data query.
 *
 * @param <T> The type of data in a cache result.
 */
public final class CacheResult<T> {
	
	/**
	 * Whether this cache result represents a cache
	 * hit or a cache miss.
	 */
	public final boolean hit;
	
	/**
	 * The data in this cache result.
	 */
	public final T data;
	
	/**
	 * Constructs a new CacheResult representing a
	 * cache miss.
	 */
	CacheResult() {
		this.hit  = false;
		this.data = null;
	}
	
	/**
	 * Constructs a new CacheResult representing a
	 * cache hit with the given data.
	 *
	 * @param data The data to attach to the
	 *             constructed cache result.
	 */
	CacheResult(@Nullable T data) {
		this.hit  = true;
		this.data = data;
	}
	
}
