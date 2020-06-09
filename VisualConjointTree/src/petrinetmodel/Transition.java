package petrinetmodel;

public class Transition extends Node {

	final static String AND_OPEN = "\\( ";
	final static String AND_CLOSE = "\\) ";
	
	public Transition(String s) {
		super(s.toLowerCase());
	}

	@Override
	protected String getFork() {
		return AND_OPEN;
	}

	@Override
	protected String getJoin() {
		return AND_CLOSE;
	}

}
