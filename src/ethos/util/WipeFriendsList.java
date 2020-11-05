package ethos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class WipeFriendsList {

	public static void main(String[] args) {
		File[] folder = new File("./Data/characters").listFiles();
		for (File subfile : folder) {
			if (Objects.isNull(subfile) || !subfile.getName().endsWith(".txt")) {
				continue;
			}
			Collection<String> lines = new ArrayList<String>();
			try (BufferedReader reader = new BufferedReader(new FileReader(subfile))) {
				String line;
				String key = "";
				while ((line = reader.readLine()) != null) {
					if (!line.contains("=")) {
						lines.add(line);
						continue;
					}
					key = line.substring(0, line.indexOf("=")).trim();
					if (!key.equals("character-friend")) {
						lines.add(line);
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(subfile))) {
				for (String line : lines) {
					writer.write(line);
					writer.newLine();
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
