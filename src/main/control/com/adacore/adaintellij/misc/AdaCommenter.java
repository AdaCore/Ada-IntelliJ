package com.adacore.adaintellij.misc;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.analysis.lexical.AdaTokenTypes;

/**
 * Ada code commenter.
 */
public class AdaCommenter implements Commenter {

	/**
	 * @see com.intellij.lang.Commenter#getLineCommentPrefix()
	 *
	 * Note: Two spaces are added after the comment prefix to
	 *       conform to the standard GNAT coding style.
	 */
	@Nullable
	@Override
	public String getLineCommentPrefix() {
		return AdaTokenTypes.COMMENT_PREFIX + "  ";
	}

	/**
	 * @see com.intellij.lang.Commenter#getBlockCommentPrefix()
	 */
	@Nullable
	@Override
	public String getBlockCommentPrefix() { return null; }

	/**
	 * @see com.intellij.lang.Commenter#getBlockCommentSuffix()
	 */
	@Nullable
	@Override
	public String getBlockCommentSuffix() { return null; }

	/**
	 * @see com.intellij.lang.Commenter#getCommentedBlockCommentPrefix()
	 */
	@Nullable
	@Override
	public String getCommentedBlockCommentPrefix() { return null; }

	/**
	 * @see com.intellij.lang.Commenter#getCommentedBlockCommentSuffix()
	 */
	@Nullable
	@Override
	public String getCommentedBlockCommentSuffix() { return null; }

}
