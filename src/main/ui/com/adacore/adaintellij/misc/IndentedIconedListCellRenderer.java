package com.adacore.adaintellij.misc;

import java.awt.*;
import javax.swing.*;

import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.UIUtils;

/**
 * List cell renderer adding a configurable icon and
 * discretized indentation.
 */
public class IndentedIconedListCellRenderer extends BasicListCellRenderer {
	
	/**
	 * Cell content indentation factor.
	 */
	private static int INDENTATION_FACTOR = 20;
	
	/**
	 * Cell content indentation.
	 */
	private int indentation;
	
	/**
	 * Cell icon.
	 */
	private Icon icon;
	
	/**
	 * Constructs a new IndentedIconedListCellRenderer given
	 * an indentation level and an icon.
	 *
	 * @param indentation The indentation level to set for
	 *                    list cells.
	 * @param icon The icon to attach to list cells.
	 */
	public IndentedIconedListCellRenderer(
		         int  indentation,
		@NotNull Icon icon
	) {
		this.indentation = indentation;
		this.icon        = icon;
	}
	
	/**
	 * @see BasicListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(
		JList<?> list,
		Object   value,
		int      index,
		boolean  isSelected,
		boolean  cellHasFocus
	) {
		
		JLabel label = (JLabel)super.getListCellRendererComponent(
			list, value, index, isSelected, cellHasFocus);
		
		UIUtils.addIconWithGap(label, icon);
		label.setBorder(JBUI.Borders.empty(2, 2 + indentation * INDENTATION_FACTOR, 2, 2));
		
		return label;
		
	}
	
}
