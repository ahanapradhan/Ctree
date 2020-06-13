package util;

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

}
