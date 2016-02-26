package com.tongxue.connector.Objs;

import java.util.Random;

/**
 *
 * @author Newnius
 */
public class User {
	private String username;
	private String password;
	private String email;
	private String sid;

	public User(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public static boolean validateEmail(String email) {
		if (email == null) {
			return false;
		}
		if (email.length() > 45) {
			return false;
		}
		String regex = "[0-9A-Za-z\\-_\\.]+@[0-9a-z]+\\.edu(.cn)?";
		return email.matches(regex);
	}

	public static boolean validateUsername(String username) {
		if (username == null) {
			return false;
		}

		if (username.contains("@")) {
			return false;
		}

		if (username.length() < 1 || username.length() > 12) {
			return false;
		}
		return true;
	}


	public static String cryptPwd(String password) {
		String str = randomString(32);
		
		return password;
	}

	private static String randomString(int length) {
		String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			str += chars.charAt(random.nextInt(chars.length() + 1));
		}
		return str;
	}

}
