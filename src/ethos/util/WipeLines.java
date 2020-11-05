package ethos.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class WipeLines {

	private final static String learningFile = "linestokeep.txt";

	public static TreeSet<String> linesToKeep() {
		try {
			TreeSet<String> linesToKeep = new TreeSet<>(new Comparator<String>() {
				@Override
				public int compare(String arg0, String arg1) {
					return arg0.compareTo(arg1);
				}
			});

			BufferedReader br = new BufferedReader(new FileReader(learningFile));
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.equals("")) {
					String[] args = line.split("\t");
					linesToKeep.add(args[0].split(" ")[0].toLowerCase().trim());
				}
			}
			br.close();
			System.out.println("Succesfully read " + learningFile);
			return linesToKeep;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void wipeLines(TreeSet<String> linesToKeep) {
		long start = System.currentTimeMillis();
		long success = 0;
		long failed = 0;
		List<String> failedFiles = new ArrayList<>();
		long linesDeleted = 0;
		long linesKept = 0;
		File[] folder = new File("./Data/characters/").listFiles();
		for (File subfile : folder) {
			if (Objects.isNull(subfile) || !subfile.getName().endsWith(".txt") || subfile.getName().equals(learningFile)) {
				continue;
			}
			Collection<String> lines = new ArrayList<>();
			try (BufferedReader reader = new BufferedReader(new FileReader(subfile))) {
				String line;
				boolean firstLine = true;
				while ((line = reader.readLine()) != null) {
					if (line.trim().equals("")) {
						continue;
					}
					String[] args = line.split("\t");
					if (linesToKeep.contains(args[0].split(" ")[0].toLowerCase().trim())) {
						if (line.startsWith("[") && line.endsWith("]")) {
							if (firstLine) {
								firstLine = false;
							} else {
								lines.add("");
								linesKept++;
							}
						}
						lines.add(line);
						linesKept++;
					} else {
						linesDeleted++;
					}
				}
				reader.close();
			} catch (IOException e) {
				failed++;
				failedFiles.add(subfile.getName());
				continue;
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(subfile))) {
				for (String line : lines) {
					writer.write(line);
					writer.newLine();
				}
				writer.close();
				success++;
			} catch (IOException e) {
				failed++;
				failedFiles.add(subfile.getName());
				e.printStackTrace();
			}
		}
		System.out.println();
		System.out.println("Finished wiping lines in " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Amount of files checked: " + (success + failed));
		System.out.println("Amount of success: " + success + " (" + (Math.round(success / (success + failed) * 1000)) / 10 + "%)");
		System.out.println("Amount of failures: " + failed + " (" + (Math.round(failed / (success + failed) * 1000)) / 10 + "%)");
		System.out.println("Amount of lines kept: " + linesKept);
		System.out.println("Amount of lines deleted: " + linesDeleted);
		System.out.println();
		if (failedFiles.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("Failed to check the following files: " + failedFiles.get(0));
			if (failedFiles.size() > 1) {
				for (int i = 1; i < failedFiles.size(); i++) {
					sb.append(", " + failedFiles.get(i));
				}
			}
			System.out.println(sb.toString());
		}

	}

	public static void main(String[] args) {
		TreeSet<String> linesToKeep = linesToKeep();
		if (linesToKeep == null) {
			System.out.println("Failed to load the file containing the lines to keep.");
		} else {
			wipeLines(linesToKeep);
		}
		System.out.println("Press any key to exit...");
	}

}
