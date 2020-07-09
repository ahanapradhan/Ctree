package util;

public interface IUtils {
	final String PIPE_XML_NET_PATTERN_REGEX = "<net id=\"([^\"]*)\"";
	final String PIPE_XML_PLACE_PATTERN_REGEX = "<place id=\"([^\"]*)\">";
	final String PIPE_XML_TRANSITION_PATTERN_REGEX = "<transition id=\"([^\"]*)\">";
	final String PIPE_XML_ARC_PATTERN_REGEX = "<arc id=\"([^\"]*)\" source=\"([^\"]*)\" target=\"([^\"]*)\"";
	final String ORIGINAL_CTREE_NAME = "Original Ctree";

	final static String XOR_OPEN = "\\[ ";
	final static String XOR_CLOSE = "\\] ";	
	final static String LOOP_OPEN = "\\{ ";
	final static String LOOP_CLOSE = "\\} ";	
	final static String AND_OPEN = "\\( ";
	final static String AND_CLOSE = "\\) ";
	
	final int ONLY_INDEX = 0;
	final int FIRST_INDEX = 0;
	final int SECOND_INDEX = 1;
	final int ZERO = 0;
	final int ONE = 1;
	final int MORE_THAN_ONE = 2;
	
	final int SINGLE_IN_OUT = 1;
	final int SINGLE_IN_MULTI_OUT = 2;
}
