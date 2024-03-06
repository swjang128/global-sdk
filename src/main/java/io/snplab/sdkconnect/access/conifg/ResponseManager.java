package io.snplab.sdkconnect.access.conifg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ResponseManager {
	// Common
	SUCCESS(200, "Success"),
	CREATED(201, "Created"),
	// Account
	DUPLICATE_EMAIL(1000, "Duplicate email"),
	NON_DUPLICATE_EMAIL(1004, "Non duplicate email");

	public final int code;
	public final String message;
}
