package petrinetmodel;

import util.IUtils;

public class Place extends Node {

	int unvisitedLoopArcs;
	boolean loopCheck = false;
	boolean hasLoopArc = false;

	public Place(String s) {
		super(s.toLowerCase());
		unvisitedLoopArcs = 0;
	}

	public boolean doesItHaveLoopArc() {
		if (!loopCheck && getInArcs() != null) {
			for (Arc i : getInArcs()) {
				Node preTrans = i.getInNode();
				if (getPostNodes() != null && getPostNodes().contains(preTrans)) {
					hasLoopArc = true;
					unvisitedLoopArcs++;
				}
			}
			loopCheck = true;
			unvisitedInArcs -= unvisitedLoopArcs;
		}
		return hasLoopArc;
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
