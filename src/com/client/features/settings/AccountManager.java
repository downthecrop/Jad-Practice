package com.client.features.settings;

import com.client.sign.Signlink;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Zion
 * 
 * This class manages all stored accounts
 * 
 */
public class AccountManager {
	
	/**
	 * Account file save location
	 */
	private static File saveAccountFile = new File(Signlink.getCacheDirectory() + "accounts.dat");
	
	/**
	 * Stores the accounts
	 */
	public static List<AccountData> accounts = new LinkedList<AccountData>();
	
	/**
	 * Gets all the stored accounts in the map
	 * 
	 * @return
	 */
	public static List<AccountData> getAccounts() {
		return accounts;
	}
	
	/**
	 * Gets account for specified name
	 * 
	 * @param username
	 * @return
	 */
	public static AccountData getAccount(String username) {
		for (AccountData account : accounts) {
			if (account.username.equalsIgnoreCase(username)) {
				return account;
			}
		}
		return null;
	}
	
	/**
	 *	Adds the account to the account map
	 *
	 * @param account
	 */
	public static void addAccount(AccountData account) {
		if (!accounts.isEmpty()) {
			for (int size = 0; size < accounts.size(); size++) {
				if (accounts.get(size).username.equalsIgnoreCase(account.username)) {
					saveAccount();
					return;
				}
			}
		}
		if (account.username == null && account.username.length() <= 0 || account.password == null && account.password.length() <= 0) {
			return;
		}
		accounts.add(account);
		saveAccount();
	}
	
	/**
	 * Clears the account map
	 */
	public static void clearAccountList() {
		accounts.clear();
		saveAccount();
	}
	
	/**
	 * Removes a desired account from the <accounts> map
	 * 
	 * @param account
	 */
	public static void removeAccount(AccountData account) {
		if (!accounts.contains(account)) {
			return;
		}
		accounts.remove(account);
		saveAccount();
	}
	
	/**
	 * Saves the account
	 */
	public static void saveAccount() {
		if (accounts == null || accounts.isEmpty()) {
			return;
		}
		try {
			DataOutputStream output = new DataOutputStream(new FileOutputStream(saveAccountFile));
			output.writeByte(accounts.size());
			for (int index = 0; index < accounts.size(); index++) {
				AccountData account = accounts.get(index);
				output.writeUTF(account.username);
				output.writeUTF(account.password);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads the account
	 */
	public static void loadAccount() {
		if (!saveAccountFile.exists()) {
			return;
		}
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(saveAccountFile));
			int fileSize = input.readByte();
			for (int index = 0; index < fileSize; index++) {
				String username = input.readUTF();
				String password = input.readUTF();
				AccountData account = new AccountData(username, password);
				accounts.add(account);
			}
			input.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}