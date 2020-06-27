package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringUtils {
	
	public static String clearWhitespaces(String s) {
		return s.trim();
	}
	
	public static String getReverseSequenceNet(String s) {
		String[] items = s.split(" ");
		StringBuilder sb = new StringBuilder();
		for(int i = items.length - 1; i >=0; i--) {
			sb.append(items[i] + " ");
		}
		return sb.toString();
	}
	
	public static StringBuilder removeDuplicates(StringBuilder sb) {
		String newLabel = sb.toString();
		String[] places = newLabel.split(",");
		Set<String> uniqPlaces = new HashSet<String>(Arrays.asList(places));
		sb = new StringBuilder();
		String prefix = "";
		for(String s : uniqPlaces) {
			sb.append(prefix+s);
			prefix = ",";
		}
		return sb;
	}

}
