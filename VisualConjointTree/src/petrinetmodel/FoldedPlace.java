package petrinetmodel;

import util.IUtils;
import util.StringUtils;

public class FoldedPlace {
	private Place p;

	boolean hasFoldedLabel = false;

	public FoldedPlace(Place p) {
		this.p = p;
	}

	public String getFoldedLabel() {
		if (!hasFoldedLabel) {
			buildFoldedLabel();
		}
		return p.getLabel();
	}

	private void buildFoldedLabel() {
		StringBuilder sb = new StringBuilder();
		sb.append(p.getLabel());

		if (p.getOutArcs() != null) {
			for (Arc a : p.getOutArcs()) {
				Transition postt = (Transition) a.getOutNode();
				if (postt.isItOneInOneOut()) {
					Place fp = (Place) postt.getOutArcs().get(IUtils.FIRST_INDEX).getOutNode();
					FoldedPlace fpp = new FoldedPlace(fp);
					String plabel = fpp.getFoldedLabel();
					sb.append("," + plabel);
				}
			}
		}
		hasFoldedLabel = true;
		sb = StringUtils.removeDuplicates(sb);
		p.setLabel(sb.toString());
	}
}
