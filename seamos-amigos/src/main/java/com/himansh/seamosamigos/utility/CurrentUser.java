package com.himansh.seamosamigos.utility;

import lombok.extern.slf4j.Slf4j;

/**
 * To store the current logged in user id.
 * @author Himansh
 *
 */
@Slf4j
public class CurrentUser {
	private static final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

	public static Integer getCurrentUserId() {
		return CurrentUser.currentUserId.get();
	}

	public static void setCurrentUserId(Integer userId) {
		CurrentUser.currentUserId.set(userId);
	}

	public static void clear() {
		log.info("Clearing Current Thread UserContext.");
		currentUserId.remove();
	}
}
