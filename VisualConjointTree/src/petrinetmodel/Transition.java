package petrinetmodel;

public class Transition extends Node {

	public Transition(String s) {
		super(s.toLowerCase());
	}

	@Override
	protected String getOpen() {
		return "\\( ";
	}

	@Override
	protected String getClose() {
		return "\\) ";
	}

}
