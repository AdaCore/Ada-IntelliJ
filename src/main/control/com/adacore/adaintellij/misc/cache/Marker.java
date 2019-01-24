package com.adacore.adaintellij.misc.cache;

import org.jetbrains.annotations.*;

/**
 * Marker used to mark markables.
 * @see com.adacore.adaintellij.misc.cache.Markable
 */
public final class Marker extends CacheKey<Object> {
	
	/**
	 * Constructs a new unique Marker.
	 */
	private Marker() {}
	
	/**
	 * Factory method that returns a new marker.
	 *
	 * @return A new unique marker.
	 */
	@Contract(" -> new")
	@NotNull
	public static Marker getNewMarker() { return new Marker(); }

}
