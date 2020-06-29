package petrinetmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.IUtils;
import util.StringUtils;

public class FoldedPlace {
	private Place p;

	boolean hasFoldedLabel = false;

	public FoldedPlace(Place p) {
		this.p = p;
	}
	
	public Place getPlace() {
		return p;
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

	public Set<Place> foldPostNodes() {
		List<Arc> pinitOutArcs = p.getOutArcs();
		List<Arc> arcstoremove = new ArrayList<Arc>();
		List<Arc> arcstoadd = new ArrayList<Arc>();
		Set<Place> placetoremove = new HashSet<Place>(); // net has to remove this set
		if (pinitOutArcs != null) {
			for (Arc a : pinitOutArcs) {
				Transition postt = (Transition) a.getOutNode();
				if (postt.isItOneInOneOut()) {
					Place postp = (Place) postt.getOutArcs().get(IUtils.FIRST_INDEX).getOutNode();
					p.setLabel(p.getLabel() + "," + postp.getLabel());
					arcstoremove.add(a);
					placetoremove.add(postp);

					if (postp.getOutArcs() != null) {
						for (Arc nexta : postp.getOutArcs()) {
							nexta.setInNode(p);
							arcstoadd.add(nexta);
						}
					}
				}
			}
		}
		for (Arc a : arcstoremove) {
			p.removeOutArc(a);
		}
		for (Arc a : arcstoadd) {
			p.addOutArc(a);
		}
		p.setLabel(StringUtils.removeDuplicates(p.getLabel()));
		return placetoremove;
	}
}
