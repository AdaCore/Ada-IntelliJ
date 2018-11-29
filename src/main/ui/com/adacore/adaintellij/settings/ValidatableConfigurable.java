package com.adacore.adaintellij.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

/**
 * A configurable whose contents are validated before being applied.
 */
public interface ValidatableConfigurable extends Configurable {
	
	/**
	 * Checks the contents of this configurable and throws a
	 * `ConfigurationException` if they are not valid.
	 * By default, this method succeeds silently.
	 *
	 * @throws ConfigurationException If the contents of this configurable
	 *                                are not valid.
	 */
	default void validateConfigurable() throws ConfigurationException {}
	
	/**
	 * Applies the configurable contents to the target settings.
	 * Implementations of this method do not need to worry about
	 * validation which is done in `apply`. The latter only calls
	 * this method if the configurable has been successfully
	 * validated.
	 */
	void applyAfterValidation();
	
	/**
	 * @see com.intellij.openapi.options.Configurable#apply()
	 */
	@Override
	default void apply() throws ConfigurationException {
	
		// First validate the configurable
		
		validateConfigurable();
		
		// If this point is reached, then the configurable has been
		// successfully validated, so apply the configurable
		
		applyAfterValidation();
	
	}
	
}
