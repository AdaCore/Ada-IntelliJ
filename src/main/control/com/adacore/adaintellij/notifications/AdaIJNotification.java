package com.adacore.adaintellij.notifications;

import com.intellij.notification.*;
import org.jetbrains.annotations.NotNull;

/**
 * A notification originating from the Ada-IntelliJ plugin.
 */
public final class AdaIJNotification extends Notification {
	
	/**
	 * The group display ID used for this plugin.
	 */
	private static final String GROUP_DISPLAY_ID = "Ada-IntelliJ Plugin";
	
	/**
	 * Constructs a new Ada-IntelliJ notification given a title,
	 * notification content, and a notification type.
	 *
	 * @param title The notification title.
	 * @param content The notification content.
	 * @param type The notification type.
	 */
	public AdaIJNotification(
		@NotNull String title,
		@NotNull String content,
		@NotNull NotificationType type)
	{ super(GROUP_DISPLAY_ID, GROUP_DISPLAY_ID + " - " + title, content, type); }

}
