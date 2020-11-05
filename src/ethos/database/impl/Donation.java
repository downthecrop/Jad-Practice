package ethos.database.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Donation extends Thread implements Runnable {
	
	@Override
	public void run() {
		try {
		    JsonArray payments = getJsonData();
		    payments.getAsJsonArray().forEach(item -> {
		    	Payment p = new Payment(item.getAsJsonObject());
		    	System.out.println("[Donation] "+p.getPlayerName()+" has claimed x"+p.getQuantity()+" "+p.getItemName()+"!");
		    });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JsonArray getJsonData() {
        try {     
        	URL url = new URL("https://ascend-ps.com//payment/verify/king+fox");
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			
			JsonParser jp = new JsonParser();
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		    request.disconnect();
		    return root.getAsJsonArray();
        } catch (Exception e) {
        	e.printStackTrace();
        	return null;
        }
	}

	public class Payment {
			
		private int payId;
		private String itemName;
		private int item_number;
		private String status;
		private double amount;
		private int quantity;
		private String currency;
		private String buyer;
		private String receiver;
		private String dateline;
		private String player_name;
		private int claimed;
		
		public Payment(JsonObject payment) {
			this.payId = payment.get("id").getAsInt();
			this.itemName = payment.get("item_name").getAsString();
			this.item_number = payment.get("item_number").getAsInt();
			this.status = payment.get("status").getAsString();
			this.amount = payment.get("amount").getAsInt();
			this.quantity = payment.get("quantity").getAsInt();
			this.currency = payment.get("currency").getAsString();
			this.buyer = payment.get("buyer").getAsString();
			this.receiver = payment.get("receiver").getAsString();
			this.dateline = payment.get("dateline").getAsString();
			this.player_name = payment.get("player_name").getAsString();
			this.claimed = payment.get("claimed").getAsInt();
		}
		
		public int getPayId() {
			return payId;
		}
		
		public String getItemName() {
			return itemName;
		}
		
		public int getItemNumber() {
			return item_number;
		}
		
		public String getStatus() {
			return status;
		}
		
		public double getAmount() {
			return amount;
		}
		
		public int getQuantity() {
			return quantity;
		}
		
		public String getCurrency() {
			return currency;
		}
		
		public String getBuyer() {
			return buyer;
		}
		
		public String getReceiver() {
			return receiver;
		}
		
		public String getDateline() {
			return dateline;
		}
		
		public String getPlayerName() {
			return player_name;
		}
		
		public int getClaimed() {
			return claimed;
		}
		
		public String toString() {
			String data = ToStringBuilder.reflectionToString(this);
			int start = data.indexOf("[");
			int end = data.indexOf("]");
			return data.substring(start, end);
		}
		
		
	}
}