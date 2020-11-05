package com.client;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.client.sign.Signlink;

public class GroundItemColors implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int itemColor = 0xffffff;
	public int itemId = 0;

	public GroundItemColors(int id, int color) {
		this.itemId = id;
		this.itemColor = color;
	}

	public static void saveGroundItems(ArrayList<GroundItemColors> itemColors) {
		try {
			@SuppressWarnings("unused")
			File file = new File(Signlink.getCacheDirectory() + "grounditems.ser");
			FileOutputStream fileOut = new FileOutputStream(Signlink.getCacheDirectory() + "grounditems.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(itemColors);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadGroundItems() {
		ArrayList<GroundItemColors> itemColors = null;
		try {
			FileInputStream fileIn = new FileInputStream(Signlink.getCacheDirectory() + "grounditems.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			itemColors = (ArrayList<GroundItemColors>) in.readObject();
			in.close();
			fileIn.close();
			Client.groundItemColors = itemColors;
		} catch (IOException | ClassNotFoundException i) {
			return;
		}
	}
}
