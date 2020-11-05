package ethos.util;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.math.NumberUtils;
import org.jboss.netty.buffer.ChannelBuffer;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import ethos.model.items.GameItem;
import ethos.model.players.Player;
import ethos.model.players.Right;

public class Misc {
	
	public static void deleteFromSB(StringBuilder sb, String s) {
	    int start = sb.indexOf(s);
	    if(start < 0)
	        return;

	    sb.delete(start, start + s.length());
	}
	
	public static boolean tryParseInt(String value) {  
	     try {  
	         Integer.parseInt(value);  
	         return true;  
	      } catch (NumberFormatException e) {  
	         return false;  
	      }  
	}

	public static int getManhattanDistance(int x, int y, int x2, int y2) {
		return Math.abs(x - x2) + Math.abs(y - y2);
	}
	
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	
	public static int randomOffset(int inclusive, int inclusiveOffset) {
		int randomOffset = random(inclusiveOffset);

		return Misc.random(1) == 0 ? inclusive - randomOffset : inclusive + randomOffset;
	}
	
	public static <K, V> Map<K, V> map(Collection<K> keys, Collection<V> values) {

	    Map<K, V> map = new HashMap<K, V>();
	    Iterator<K> keyIt = keys.iterator();
	    Iterator<V> valueIt = values.iterator();
	    while (keyIt.hasNext() && valueIt.hasNext()) {
	        K k = keyIt.next();
	        if (null != map.put(k, valueIt.next())){
	            throw new IllegalArgumentException("Keys are not unique! Key " + k + " found more then once.");
	        }
	    }
	    if (keyIt.hasNext() || valueIt.hasNext()) {
	        throw new IllegalArgumentException("Keys and values collections have not the same size");
	    };

	    return map;
	}

	/**
	 * Creates a new {@link String} array with the specified length with non-null empty elements.
	 * 
	 * @param length the length of the array
	 * @return a new array with all empty elements
	 */
	public static String[] nullToEmpty(int length) {
		String[] output = new String[length];
		Arrays.fill(output, 0, length, "");
		return output;
	}

	/**
	 * Used to determine if the value of the input is a non-negative value. This does permit the value zero as valid input as zero is neither positive nor negative.
	 * 
	 * @param input the input we're trying to determine is a non-negative or not
	 * @return {@code} true if the value is greater than negative one, otherwise {@code false}.
	 */
	public static boolean isNonNegative(int input) {
		return input > -1;
	}

	/**
	 * Used to determine if all the values of the input array are a non-negative values. This does permit the value zero as valid input as zero is neither positive nor negative.
	 * 
	 * @param input the input we're trying to determine is a non-negative or not
	 * @return {@code} true if the values are greater than negative one, otherwise {@code false}.
	 */
	public static boolean isNonNegative(int[] input) {
		for (int value : input) {
			if (!isNonNegative(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines if the random value returned from range is greater than or equal to the inclusive value of checkpoint.
	 * 
	 * @param range the range of integer values.
	 * 
	 * @param checkpoint the checkpoint to be made.
	 * 
	 * @return {@code true} if checkpoint is greater than the. random value returned,otherwise {@code false}.
	 * 
	 * @throws IllegalStateException thrown if the checkpoint value is less than the minimum range, or greater than the maximum range.
	 */
	public static boolean passedProbability(Range<Integer> range, int checkpoint, boolean ignoreMaximum) throws IllegalStateException {
		if (checkpoint < range.getMinimum() || checkpoint > range.getMaximum() && !ignoreMaximum) {
			throw new IllegalStateException();
		}
		return random(range) >= checkpoint;
	}

	public static boolean passedProbability(Range<Integer> range, int checkpoint) throws IllegalStateException {
		return passedProbability(range, checkpoint, false);
	}

	public static int random(Range<Integer> range) {
		int minimum = range.getMinimum();
		return minimum + random(range.getMaximum() - minimum);
	}

	public static int randomSearch(int[] elements, int inclusive, int exclusiveLength) {
		Preconditions.checkArgument(exclusiveLength <= elements.length, "The length specified is greater than the length of the array.");
		return elements[RandomUtils.nextInt(inclusive, exclusiveLength)];
	}

	public static <T> T randomSearch(T[] elements, int inclusive, int exclusiveLength) {
		Preconditions.checkArgument(exclusiveLength <= elements.length, "The length specified is greater than the length of the array.");
		return elements[RandomUtils.nextInt(inclusive, exclusiveLength)];
	}
	
	public static <T> T randomTypeOfList(List<T> list) {
		return list.get(new SecureRandom().nextInt(list.size()));
	}

	public static <T> List<T> jsonArrayToList(Path path, Class<T[]> clazz) {
		try {
			T[] collection = new Gson().fromJson(Files.newBufferedReader(path), clazz);
			return new ArrayList<T>(Arrays.asList(collection));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> jsonObjectToList(Path path, Class<T> clazz) {
		try {
			T collection = new Gson().fromJson(Files.newBufferedReader(path), clazz);
			return new ArrayList<T>(Arrays.asList(collection));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static GameItem getItemFromList(List<GameItem> list) {
		if (list.isEmpty()) {
			return null;
		}

		return list.get(RandomUtils.nextInt(0, list.size()));

	}

	public static double preciseRandom(Range<Double> range) {
		Preconditions.checkArgument(range.getMinimum() <= range.getMaximum(), "The maximum range cannot be less than the minimum range.");
		return range.getMinimum() + (new Random().nextDouble() * (range.getMaximum() - range.getMinimum()));
	}

	public static Date getFutureDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute, second);
		return calendar.getTime();
	}

	public static String toFormattedHMS(long time) {
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
				TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}

	public static String toFormattedMS(long time) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}

	/**
	 * A linear search is conducted by looping through the elements array and looking for the index that contains the value specified.
	 * 
	 * @param elements the elements we're searching through
	 * @param value the value we're searching for
	 * @return -1 if the value cannot be found in the array, otherwise the index of the value.
	 */
	public static int linearSearch(int[] elements, int value) {
		for (int index = 0; index < elements.length; index++) {
			if (elements[index] == value) {
				return index;
			}
		}
		return -1;
	}

	public static <T> int linearSearch(T[] elements, T value) {
		for (int index = 0; index < elements.length; index++) {
			if (elements[index].equals(value)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Determines if a given String is a valid IPv4 Address
	 * 
	 * @param input The String which is to be checked
	 * @return
	 */
	public static boolean isIPv4Address(String input) {
		String[] segments = input.split("\\.");
		if (segments.length != 4) {
			return false;
		}
		for (String token : segments) {
			if (!NumberUtils.isNumber(token)) {
				return false;
			}
			int value = Integer.parseInt(token);
			if (value < 0 || value > 255) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the IP of a {@link Player} if they are not null or a member of the staff team.
	 * 
	 * @param player The player of which we want to obtain their IP.
	 * @return A String which is safe to show to other staff members.
	 */
	public static String getIP(Player player) {
		return player == null ? "Unknown" : player.getRights().isOrInherits(Right.MODERATOR) ? "Private" : player.connectedFrom;
	}

	/**
	 * This function determines if any of the elements in any of the collections have an element that are equal to one another.
	 * 
	 * @param collections
	 * @return
	 */
	@SafeVarargs
	public static <T> boolean elementsMatch(Collection<T>... collections) {
		for (Collection<T> collection : collections) {
			for (Collection<T> otherCollection : collections) {
				if (otherCollection.equals(collection)) {
					continue;
				}
				for (T element : otherCollection) {
					if (collection.contains(element)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int indexOfPartialString(List<String> list, String s) {
		if (s == null || list == null) {
			return -1;
		}
		for (int i = 0; i < list.size(); i++) {
			String element = list.get(i);
			if (element.startsWith(s) || element.equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}

	public static int lastIndexOfPartialString(List<String> list, String s) {
		if (s == null || list == null) {
			return -1;
		}
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			String element = list.get(i);
			if (element.startsWith(s) || element.equalsIgnoreCase(s)) {
				index = i;
			}
		}
		return index;
	}

	@Deprecated
	public static int toCycles(long time, TimeUnit unit) {
		return (int) TimeUnit.MILLISECONDS.convert(time, unit) / 600;
	}

	public static int toCyclesOrDefault(long time, int def, TimeUnit unit) {
		if (time > Integer.MAX_VALUE) {
			time = def;
		}
		return (int) TimeUnit.MILLISECONDS.convert(time, unit) / 600;
	}

	public static long cyclesToMinutes(int cycles) {
		return cycles / 100;
	}

	public static GameItem getRandomItem(GameItem[] itemArray) {
		return itemArray[random(itemArray.length - 1)];
	}

	public static GameItem getRandomItem(List<GameItem> itemArray) {
		return itemArray.get(random(itemArray.size() - 1));
	}

	public static int combatDifference(Player player, Player player2) {
		if (player.combatLevel > player2.combatLevel)
			return player.combatLevel - player2.combatLevel;
		else if (player.combatLevel < player2.combatLevel)
			return player2.combatLevel - player.combatLevel;
		return 0;
	}

	public static long getTime() {
		return TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public static String getRS2String(final ChannelBuffer buf) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.readable() && (b = buf.readByte()) != 10)
			bldr.append((char) b);
		return bldr.toString();
	}

	public static String insertCommas(String str) {
		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + "," + str.substring(str.length() - 3, str.length());
	}

	public static String getValueRepresentation(long amount) {
		StringBuilder bldr = new StringBuilder();
		if (amount < 1_000) {
			bldr.append(amount);
		} else if (amount >= 1_000 && amount < 1_000_000) {
			bldr.append("@cya@" + Long.toString(amount / 1_000) + "K @whi@(" + insertCommas(Long.toString(amount)) + ")");
		} else if (amount >= 1_000_000) {
			bldr.append("@gre@" + Long.toString(amount / 1_000_000) + "M @whi@(" + insertCommas(Long.toString(amount)) + ")");
		}
		return bldr.toString();
	}

	public static String getValueWithoutRepresentation(long amount) {
		StringBuilder bldr = new StringBuilder();
		if (amount < 1_000) {
			bldr.append(amount);
		} else if (amount >= 1_000 && amount < 1_000_000) {
			bldr.append(Long.toString(amount / 1_000) + "K");
		} else if (amount >= 1_000_000) {
			bldr.append(Long.toString(amount / 1_000_000) + "M");
		}
		return bldr.toString();
	}

	public static double randomDouble(double min, double max) {
		return (Math.random() * (max - min) + min);
	}

	public static String formatPlayerName(String str) {
		str = capitalizeJustFirst(str);
		str.replace("_", " ");
		return str;
	}

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

	public static String md5Hash(String md5) {
		try {

			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}

			return sb.toString();

		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String removeSpaces(String s) {
		return s.replace(" ", "");
	}

	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public static String findCommand(String message) {
		if (!message.contains(" ") && !message.contains("-")) {
			return message;
		} else if (!message.contains(" ")) {
			return message.substring(0, message.indexOf("-"));
		} else if (!message.contains("-")) {
			return message.substring(0, message.indexOf(" "));
		}
		int seperatorIndex = message.indexOf(" ") < message.indexOf("-") ? message.indexOf(" ") : message.indexOf("-");
		return message.substring(0, seperatorIndex);
	}

	public static String findInput(String message) {
		if (!message.contains(" ") && !message.contains("-")) {
			return "";
		} else if (!message.contains(" ")) {
			return message.substring(message.indexOf("-") + 1);
		} else if (!message.contains("-")) {
			return message.substring(message.indexOf(" ") + 1);
		}
		int seperatorIndex = message.indexOf(" ") < message.indexOf("-") ? message.indexOf(" ") : message.indexOf("-");
		return message.substring(seperatorIndex + 1);
	}

	public static int stringToInt(String value) throws NumberFormatException {
		value = value.toLowerCase();
		value = value.replaceAll("k", "000");
		value = value.replaceAll("m", "000000");
		value = value.replaceAll("b", "000000000");
		BigInteger bi = new BigInteger(value);
		if (bi.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			return Integer.MAX_VALUE;
		} else if (bi.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
			return Integer.MIN_VALUE;
		} else {
			return bi.intValue();
		}
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance) && (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public static String longToReportPlayerName(long l) {
		int i = 0;
		final char ac[] = new char[12];
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			ac[11 - i++] = Misc.playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String longToPlayerName(long l) {
		int i = 0;
		char ac[] = new char[12];

		while (l != 0L) {
			long l1 = l;

			l /= 37L;
			ac[11 - i++] = xlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static final char playerNameXlateTable[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '[', ']', '/', '-', ' ' };

	public static String longToPlayerName2(long l) {
		int i = 0;
		char ac[] = new char[99];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String capitalizeJustFirst(String str) {
		str = str.toLowerCase();
		if (str.length() > 1) {
			str = str.substring(0, 1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}

	public static void print_debug(String str) {
		System.out.print(str);
	}

	public static void println_debug(String str) {
		System.out.println(str);
	}

	public static void print(String str) {
		System.out.print(str);
	}

	public static void println(String str) {
		System.out.println(str);
	}

	public static void printlnInt(int str) {
		System.out.println(str);
	}

	public static String Hex(byte data[]) {
		return Hex(data, 0, data.length);
	}

	public static String Hex(byte data[], int offset, int len) {
		String temp = "";
		for (int cntr = 0; cntr < len; cntr++) {
			int num = data[offset + cntr] & 0xFF;
			String myStr;
			if (num < 16)
				myStr = "0";
			else
				myStr = "";
			temp += myStr + Integer.toHexString(num) + " ";
		}
		return temp.toUpperCase().trim();
	}

	public static int hexToInt(byte data[], int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			int num = (data[offset + cntr] & 0xFF) * i;
			temp += num;
			if (i > 1)
				i = i / 1000;
		}
		return temp;
	}

	public static String basicEncrypt(String s) {
		String toReturn = "";
		for (int j = 0; j < s.length(); j++) {
			toReturn += (int) s.charAt(j);
		}
		// System.out.println("Encrypt: " + toReturn);
		return toReturn;
	}

	public static int random2(int range) {
		return (int) ((java.lang.Math.random() * range) + 1);
	}

	public static int random(int min , int max) {
	    Random rand = new Random();
	    return rand.nextInt(max - min + 1) + min;
	}
	
	public static int random(int range) {
		return (int) (java.lang.Math.random() * (range + 1));
	}
	
	public static int random3(int range) {
		return (int) (java.lang.Math.random() * (range));
	}

	public static long playerNameToInt64(String s) {
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

	private static char validChars[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
			']', '>', '<', '^', '`', '~', '_', '/' };

	public static String decodeMessage(byte[] message, int size) {
		StringBuilder sb = new StringBuilder();
		boolean capitalizeNext = true;
		if (size > message.length) {
			size = message.length;
		}
		for (int i = 0; i < size; i++) {
			char c = validChars[message[i] & 0xff];
			if (capitalizeNext && c >= 'a' && c <= 'z') {
				sb.append(Character.toUpperCase(c));
				capitalizeNext = false;
			} else {
				sb.append(c);
			}
			if (c == '.' || c == '!' || c == '?') {
				capitalizeNext = true;
			}

		}
		return sb.toString();
	}

	private static char decodeBuf[] = new char[4096];

	public static String textUnpack(byte packedData[], int size) {
		int idx = 0, highNibble = -1;
		for (int i = 0; i < size * 2; i++) {
			int val = packedData[i / 2] >> (4 - 4 * (i % 2)) & 0xf;
			if (highNibble == -1) {
				if (val < 13)
					decodeBuf[idx++] = xlateTable[val];
				else
					highNibble = val;
			} else {
				decodeBuf[idx++] = xlateTable[((highNibble << 4) + val) - 195];
				highNibble = -1;
			}
		}

		return new String(decodeBuf, 0, idx);
	}

	public static String optimizeText(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)), s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public static void textPack(byte packedData[], java.lang.String text) {
		if (text.length() > 80)
			text = text.substring(0, 80);
		text = text.toLowerCase();

		int carryOverNibble = -1;
		int ofs = 0;
		for (int idx = 0; idx < text.length(); idx++) {
			char c = text.charAt(idx);
			int tableIdx = 0;
			for (int i = 0; i < xlateTable.length; i++) {
				if (c == xlateTable[i]) {
					tableIdx = i;
					break;
				}
			}
			if (tableIdx > 12)
				tableIdx += 195;
			if (carryOverNibble == -1) {
				if (tableIdx < 13)
					carryOverNibble = tableIdx;
				else
					packedData[ofs++] = (byte) (tableIdx);
			} else if (tableIdx < 13) {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + tableIdx);
				carryOverNibble = -1;
			} else {
				packedData[ofs++] = (byte) ((carryOverNibble << 4) + (tableIdx >> 4));
				carryOverNibble = tableIdx & 0xf;
			}
		}

		if (carryOverNibble != -1)
			packedData[ofs++] = (byte) (carryOverNibble << 4);
	}

	public static char xlateTable[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[',
			']' };

	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle))
			return -1;
		if (Math.signum(dx) < 0)
			angle += 180.0;
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
	}

	public static int distanceBetween(Player c1, Player c2) {
		int x = (int) Math.pow(c1.getX() - c2.getX(), 2.0D);
		int y = (int) Math.pow(c1.getY() - c2.getY(), 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	public static int distanceToPoint(int x1, int y1, int x2, int y2) {
		int x = (int) Math.pow(x1 - x2, 2.0D);
		int y = (int) Math.pow(y1 - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	public static byte directionDeltaX[] = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 };
	public static byte directionDeltaY[] = new byte[] { 1, 1, 0, -1, -1, -1, 0, 1 };
	public static byte xlateDirectionToClient[] = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };

	public enum Direction {
		NORTH, NORTH_NORTH_EAST, NORTH_EAST, EAST_NORTH_EAST, EAST, EAST_SOUTH_EAST, SOUTH_EAST, SOUTH_SOUTH_EAST, SOUTH, SOUTH_SOUTH_WEST, SOUTH_WEST, WEST_SOUTH_WEST, WEST, WEST_NORTH_WEST, NORTH_WEST, NORTH_NORTH_WEST;

		public static String getName(int ordinal) {
			if (ordinal < 0 || ordinal > Direction.values().length - 1)
				ordinal = 0;
			return Misc.capitalize(Direction.values()[ordinal].name().toLowerCase().replaceAll("_", " "));
		}
	}

	public static String anOrA(String s) {
		s = s.toLowerCase();
		if(s.equalsIgnoreCase("anchovies") || s.equalsIgnoreCase("soft clay") || s.equalsIgnoreCase("cheese") || s.equalsIgnoreCase("ball of wool") || s.equalsIgnoreCase("spice") || s.equalsIgnoreCase("steel nails") || s.equalsIgnoreCase("snape grass") || s.equalsIgnoreCase("coal"))
			return "some";
		if(s.startsWith("a") || s.startsWith("e") || s.startsWith("i") || s.startsWith("o") || s.startsWith("u")) 
			return "an";
		return "a";
	}
}
