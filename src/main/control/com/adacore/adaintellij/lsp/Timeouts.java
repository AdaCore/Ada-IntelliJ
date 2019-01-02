package com.adacore.adaintellij.lsp;

import java.util.*;

import org.jetbrains.annotations.NotNull;

/**
 * Provider of request timeouts for the various LSP methods.
 */
public final class Timeouts {
	
	/**
	 * Default request timeout.
	 */
	public static final int DEFAULT_METHOD_TIMEOUT = 3000;
	
	/**
	 * LSP-method -> timeout mapping.
	 */
	private static final Map<String, Integer> METHOD_TIMEOUTS;
	
	static {
		
		// Populate the LSP-method -> timeout mapping
		
		Map<String, Integer> methodTimeouts = new HashMap<>();
		
		methodTimeouts.put("textDocument/completion", 5000);
		methodTimeouts.put("textDocument/definition", 2000);
		methodTimeouts.put("textDocument/references", 5000);
		
		METHOD_TIMEOUTS = Collections.unmodifiableMap(methodTimeouts);
	
	}
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private Timeouts() {}
	
	/**
	 * Returns the timeout configured for the given LSP method, or
	 * the default timeout if no specific timeout is set for that
	 * method.
	 *
	 * @param method The LSP method for which to get the timeout.
	 * @return The LSP method's configured timeout.
	 */
	public static int getMethodTimeout(@NotNull String method) {
		return METHOD_TIMEOUTS.getOrDefault(method, DEFAULT_METHOD_TIMEOUT);
	}

}
