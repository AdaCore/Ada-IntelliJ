package com.adacore.adaintellij.misc.cache;

import com.intellij.openapi.util.UserDataHolder;
import org.jetbrains.annotations.*;

/**
 * A `UserDataHolder` variant that supports storing null values.
 * It does so by wrapping the actual data in a `CacheData` object
 * and passing that wrapper to the user data holder, which would
 * allow it to later distinguish non-existent data from stored
 * null values. This also allows it to clear cached data by
 * internally setting the value of a given key in the underlying
 * data holder to null.
 * This interface also provides convenience static methods for
 * caching, querying and clearing cached data from external user
 * data holders, which is useful in the case of data holders that
 * cannot be extended.
 */
public interface Cacher extends UserDataHolder {

	/**
	 * Caches the given data with the given cache key.
	 *
	 * @param key The key representing the data to cache.
	 * @param data The data to cache.
	 * @param <T> The type of the data to cache.
	 */
	default <T> void cacheData(@NotNull CacheKey<T> key, @Nullable T data) {
		cacheData(this, key, data);
	}

	/**
	 * Clears the cached data for the given key.
	 *
	 * @param key The key of the cached data to clear.
	 * @param <T> The type of cached data to clear.
	 */
	default <T> void clearCachedData(@NotNull CacheKey<T> key) {
		clearCachedData(this, key);
	}

	/**
	 * Returns the cached data for the given key as a cache
	 * result.
	 *
	 * @param key The key of the cached data to get.
	 * @param <T> The type of the cached data to get.
	 * @return The cached data as a cache result.
	 */
	@Contract("_ -> new")
	@NotNull
	default <T> CacheResult<T> getCachedData(@NotNull CacheKey<T> key) {
		return getCachedData(this, key);
	}

	/**
	 * Caches the given data with the given cache key in the
	 * given user data holder.
	 *
	 * @param dataHolder The data holder in which to cache
	 *                   the given data.
	 * @param key The key representing the data to cache.
	 * @param data The data to cache.
	 * @param <T> The type of the data to cache.
	 */
	static <T> void cacheData(
		@NotNull  UserDataHolder dataHolder,
		@NotNull  CacheKey<T>    key,
		@Nullable T              data
	) {
		dataHolder.putUserData(key, new CacheData<>(data));
	}

	/**
	 * Clears the cached data for the given key in the given
	 * user data holder.
	 *
	 * @param dataHolder The data holder in which to clear
	 *                   the cached data.
	 * @param key The key of the cached data to clear.
	 * @param <T> The type of cached data to clear.
	 */
	static <T> void clearCachedData(
		@NotNull UserDataHolder dataHolder,
		@NotNull CacheKey<T>    key
	) {
		dataHolder.putUserData(key, null);
	}

	/**
	 * Returns the cached data for the given key in the given
	 * user data holder as a cache result.
	 *
	 * @param dataHolder The data holder from which to get the
	 *                   cached data.
	 * @param key The key of the cached data to get.
	 * @param <T> The type of the cached data to get.
	 * @return The cached data as a cache result.
	 */
	@Contract("_, _ -> new")
	@NotNull
	static <T> CacheResult<T> getCachedData(
		@NotNull UserDataHolder dataHolder,
		@NotNull CacheKey<T>    key
	) {

		CacheData<T> cacheData = dataHolder.getUserData(key);

		return cacheData == null ?
			new CacheResult<>() : new CacheResult<>(cacheData.data);

	}

}
