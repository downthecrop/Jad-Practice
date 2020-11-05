package com.client.features.settings;

/**
 * 
 * @author Zion
 * 
 * This class stores the account data
 * 
 */
public class AccountData {
	
	/**
	 * Stores account username
	 */
	public String username;
	
	/**
	 * Stores account password
	 */
	public String password;
	
	/**
	 * Creates the default account data
	 * 
	 * @param rank
	 * @param username
	 * @param password
	 */
	public AccountData(String username, String password) {
		this.username = username;
		this.password = password;
	}
}