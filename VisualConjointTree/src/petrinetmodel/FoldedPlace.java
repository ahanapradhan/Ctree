package petrinetmodel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.IUtils;

public class FoldedPlace {
	private Place p;

	boolean isFolded = false;

	public FoldedPlace(Place p) {
		this.p = p;
	}

	public String getFolding() {
		if (!isFolded) {
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
					String plabel = fpp.getFolding();
					sb.append("," + plabel);
				}
			}
		}
		isFolded = true;
		sb = removeDuplicates(sb);
		p.setLabel(sb.toString());
	}

	private StringBuilder removeDuplicates(StringBuilder sb) {
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
