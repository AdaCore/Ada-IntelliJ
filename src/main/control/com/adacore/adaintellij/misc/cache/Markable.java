package com.adacore.adaintellij.misc.cache;

import org.jetbrains.annotations.NotNull;

/**
 * A `UserDataHolder` variant that can be easily marked.
 * A markable simply behaves like a cache that stores null
 * values for marker keys.
 */
public interface Markable extends Cacher {
	
	/**
	 * Marks this markable with the given marker.
	 *
	 * @param marker The marker to apply.
	 */
	default void mark(@NotNull Marker marker) {
		cacheData(marker, null);
	}
	
	/**
	 * Removes the given marker from this markable.
	 *
	 * @param marker The marker to remove.
	 */
	default void unmark(@NotNull Marker marker) {
		clearCachedData(marker);
	}
	
	/**
	 * Returns whether or not this markable has been marked
	 * with the given marker.
	 *
	 * @param marker The marker to check in this markable.
	 * @return Whether or not this markable has been marked
	 *         with the given marker.
	 */
	default boolean isMarked(@NotNull Marker marker) {
		return getCachedData(marker).hit;
	}
	
}
