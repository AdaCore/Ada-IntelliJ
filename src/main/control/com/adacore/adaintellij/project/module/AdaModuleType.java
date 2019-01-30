package com.adacore.adaintellij.project.module;

import javax.swing.*;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.Icons;

/**
 * Ada module type. Modules of this type are configured
 * by instances of the Ada module builder.
 * @see com.adacore.adaintellij.project.module.AdaModuleBuilder
 */
public final class AdaModuleType extends ModuleType<AdaModuleBuilder> {

	/**
	 * Unique identifier of this module type.
	 */
	private static final String ID = "ADA_MODULE_TYPE";

	/**
	 * Constructs a new AdaModuleType.
	 */
	public AdaModuleType() { super(ID); }

	/**
	 * Returns an Ada module type instance.
	 *
	 * @return An Ada module type instance.
	 */
	public static AdaModuleType getInstance() {
		return (AdaModuleType)ModuleTypeManager.getInstance().findByID(ID);
	}

	/**
	 * Creates and returns a new Ada module builder.
	 *
	 * @return A new Ada module builder.
	 */
	@NotNull
	@Override
	public AdaModuleBuilder createModuleBuilder() {
		return new AdaModuleBuilder();
	}

	/**
	 * Returns the name of this module type.
	 *
	 * @return The name of this module type.
	 */
	@NotNull
	@Override
	public String getName() { return "Ada Module"; }

	/**
	 * Returns a description of this module type.
	 *
	 * @return A description of this module type.
	 */
	@NotNull
	@Override
	public String getDescription() { return "Ada Module"; }

	/**
	 * Returns the icon representing this module type.
	 *
	 * @return The icon representing this module type.
	 */
	@Override
	public Icon getIcon() { return Icons.ADA_MODULE; }

	/**
	 * @deprecated but still required by ModuleType.
	 * To be removed in the future.
	 */
	@Override
	public Icon getNodeIcon(@Deprecated boolean isOpened) {
		return getIcon();
	}

}
