package petrinetmodel;

import util.IUtils;

public class Transition extends Node {


	
	public Transition(String s) {
		super(s.toLowerCase());
	}

	@Override
	protected String getFork() {
		return IUtils.AND_OPEN;
	}

	@Override
	protected String getJoin() {
		return IUtils.AND_CLOSE;
	}

}
