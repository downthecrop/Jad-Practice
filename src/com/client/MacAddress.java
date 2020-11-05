package com.client;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 
 * @author Jason MacKeigan http://www.rune-server.org/members/jason
 * @date Jul 5, 2014, 7:04:02 AM
 */
public class MacAddress {
	/**
	 * The mac address String that will represent the 6-bit hexadecimal value
	 */
	private String macAddress;
	
	/**
	 * The MacAddress constructor that will ultimately create a String object
	 * that will represent the hexadecimal hardware address 
	 * @param address The InetAddress
	 */
	public MacAddress(InetAddress address) {
		try {
			NetworkInterface network = NetworkInterface.getByInetAddress(address);
			if(network != null) {
				StringBuilder sb = new StringBuilder();
				byte[] mac = network.getHardwareAddress();
				if(mac != null) {
					for(int i = 0; i < mac.length; i++)
						sb.append(String.format(i == mac.length - 1 ? "%02X" : "%02X-", mac[i]));
					macAddress = sb.toString();
				} else
					macAddress = "";
			} else
				macAddress = "";
		} catch (Exception e) {
			macAddress = "";
		}
	}
	
	@Override
	public String toString() {
		if(macAddress == null) {
			macAddress = "";
		}
		return macAddress;
	}
}
