package petrinetmodel;

import util.IUtils;

public class Place extends Node {

	boolean loopCheck = false;
	boolean hasLoopArc = false;

	public Place(String s) {
		super(s.toLowerCase());
	}

	@Override
	protected String getFork() {
		return IUtils.XOR_OPEN;
	}

	@Override
	protected String getJoin() {
		return IUtils.XOR_CLOSE;
	}

}
