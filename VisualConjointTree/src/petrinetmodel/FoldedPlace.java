package petrinetmodel;

import java.util.List;

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

	private Transition isThereSingleInOutTrans() {
		List<Arc> pinitOutArcs = p.getOutArcs();
		if (pinitOutArcs.size() == IUtils.ZERO) { // no post-place to fold
			return null;
		}
		for (Arc a : pinitOutArcs) {
			Transition postt = (Transition) a.getOutNode();
			if (postt.isItOneInOneOut()) {
				p.removeOutArc(a);
				return postt;
			}
		}
		return null;
	}

	public void foldSingleInOutPostTransitions(Net net) {		
		Transition postt = null;
		while ((postt = isThereSingleInOutTrans()) != null) {
			Place postp = (Place) postt.getOutArcs().get(IUtils.FIRST_INDEX).getOutNode();

			if (postp != p) {				
				modifyIncomingArcs_singleInOut(postt, postp);
				modifyOutgoingArcs_singleInOut(postp);
				net.removePlace(postp);
				net.updatePlaceLabel(p, StringUtils.removeDuplicates(p.getLabel() + "," + postp.getLabel()));
			}
			net.removeTransition(postt);
		}
	}

	private void modifyOutgoingArcs_singleInOut(Place postp) {
		if (postp.howManyOutArcs() == IUtils.ZERO) {
			// postp is sink place. nothing to do about it
			return;
		}
		for (Arc outa : postp.getOutArcs()) {
			outa.setInNode(p);
			p.addOutArc(outa);
		}
	}

	private void modifyIncomingArcs_singleInOut(Transition postt, Place postp) {
		if (postp.getInArcs().size() == IUtils.ONE) {
			// postt is the only prenode of postp. nothing to do about it
			return;
		}
		for (Arc ina : postp.getInArcs()) {
			if (ina.getInNode() != postt) {
				ina.setOutNode(p);
				p.addInArc(ina);
			}
		}
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
