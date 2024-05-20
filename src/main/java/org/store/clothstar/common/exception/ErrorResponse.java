package org.store.clothstar.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	private boolean success;
	private String message;

	public ErrorResponse(String message) {
		this.success = false;
		this.message = message;
	}
}