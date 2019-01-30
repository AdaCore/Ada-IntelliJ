package com.adacore.adaintellij.misc.cache;

import java.nio.charset.StandardCharsets;
import java.util.*;

import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.*;

/**
 * Cache key representing cached data in a `Cacher`.
 *
 * @param <T> The type of data that a key represents.
 */
public class CacheKey<T> extends Key<CacheData<T>> {

	/**
	 * Class-wide random number generator.
	 */
	private static Random RANDOM_NUMBER_GENERATOR = new Random();

	/**
	 * Constructs a new CacheKey.
	 * Initializes the parent key with a random name.
	 * @see CacheKey#getRandomName()
	 */
	protected CacheKey() { super(getRandomName()); }

	/**
	 * Factory method that returns a new cache key.
	 * @see CacheKey#CacheKey()
	 *
	 * @param <T> The type of data represented by the new key.
	 * @return A new cache key.
	 */
	@Contract(" -> new")
	@NotNull
	public static <T> CacheKey<T> getNewKey() {
		return new CacheKey<>();
	}

	/**
	 * Returns a random name to be used when constructing
	 * cache keys.
	 * The returned string is the string representation of the
	 * Base64 encoding of a random sequence of 32 bytes.
	 *
	 * @return A random key name.
	 */
	@Contract(" -> new")
	@NotNull
	private static String getRandomName() {

		byte[] bytes = new byte[32];

		RANDOM_NUMBER_GENERATOR.nextBytes(bytes);

		return new String(
			Base64.getEncoder().encode(bytes),
			StandardCharsets.UTF_8
		);

	}

}
