package com.adacore.adaintellij;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the UIUtils class.
 */
final class UIUtilsTest {
	
	// Testing UIUtils#toHtml() method
	
	@Test
	void toHtml_transforms_simple_text_correctly() {
		
		assertEquals("<html></html>",                UIUtils.toHtml(""));
		assertEquals("<html>hello</html>",           UIUtils.toHtml("hello"));
		assertEquals("<html>Hello, World!</html>",   UIUtils.toHtml("Hello, World!"));
		assertEquals("<html>abc123#@-_=+/\\</html>", UIUtils.toHtml("abc123#@-_=+/\\"));
		
	}
	
	@Test
	void toHtml_transforms_text_with_new_lines_correctly() {
		
		assertEquals("<html>Hello,<br/>World!</html>", UIUtils.toHtml("Hello,\nWorld!"));
		assertEquals("<html>a<br/>b<br/>c</html>",     UIUtils.toHtml("a\nb\nc"));
		assertEquals("<html><br/><br/><br/></html>",   UIUtils.toHtml("\n\n\n"));
		
	}
	
}
