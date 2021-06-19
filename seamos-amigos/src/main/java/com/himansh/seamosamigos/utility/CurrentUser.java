package com.himansh.seamosamigos.utility;

/**
 * To store the current logged in user id.
 * @author Himansh
 *
 */
public class CurrentUser {
	private static final ThreadLocal<Integer> currentUserId = new ThreadLocal<>();

	public static Integer getCurrentUserId() {
		return CurrentUser.currentUserId.get();
	}

	public static void setCurrentUserId(Integer userId) {
		CurrentUser.currentUserId.set(userId);
	}
}
