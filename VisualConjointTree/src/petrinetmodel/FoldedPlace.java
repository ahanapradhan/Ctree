package petrinetmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.IUtils;
import util.StringUtils;

public class FoldedPlace {
	private Place p;

	public FoldedPlace(Place p) {
		this.p = p;
	}

	public Place getPlace() {
		return p;
	}

	private Transition hasMatchingInOutPostTrans(int inNum, int outNum) {
		List<Arc> pinitOutArcs = p.getOutArcs();
		if (pinitOutArcs.size() == IUtils.ZERO) { // no post-place to fold
			return null;
		}
		for (Arc a : pinitOutArcs) {
			if (!a.isFoldVisited()) {
				Transition postt = (Transition) a.getOutNode();
				if (postt.hasMatchingInOut(inNum, outNum)) {
					a.visitForFolding();
					return postt;
				}
			}
		}
		return null;
	}

	public Set<Place> foldSingleInOutPostTransitions(Net net) {
		Set<Place> removedPlaces = new HashSet<Place>();
		Transition postt = null;
		while ((postt = hasMatchingInOutPostTrans(IUtils.ONE, IUtils.ONE)) != null) {
			Place postp = (Place) postt.getOutArcs().get(IUtils.FIRST_INDEX).getOutNode();

			if (postp != p) {
				modifyIncomingArcs_singleInOut(postt, postp);
				modifyOutgoingArcs_singleInOut(postp);
				net.removePlace(postp);
				removedPlaces.add(postp);
				net.updatePlaceLabel(p, StringUtils.removeDuplicates(p.getLabel() + "," + postp.getLabel()));
			}
			net.removeTransition(postt);
		}
		return removedPlaces;
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

	public Set<Place> foldSingleInMultiOutPostTransitions(Net net) {
		Set<Place> removedPlaces = new HashSet<Place>();
		Transition forkpostt = null;
		Transition joinpostt = null;
		Place joinp = null;
		while ((forkpostt = hasMatchingInOutPostTrans(IUtils.ONE, IUtils.MORE_THAN_ONE)) != null) {
			Set<Place> postps = new HashSet<Place>();
			for (Node n : forkpostt.getPostNodes()) {
				postps.add((Place) n);
			}
			joinpostt = Net.haveCommonPostTrans(postps);
			if (joinpostt == null) {
				Arc ua = net.findArc(p, forkpostt);
				ua.unvisitDuringFolding();
				return null;
			}

			if (joinpostt.howManyOutArcs() == IUtils.ONE) {
				joinp = (Place) joinpostt.getOutArcs().get(IUtils.FIRST_INDEX).getOutNode();
				if (joinp != p) {
					modifyIncomingArcs_singleInOut(joinpostt, joinp);
					modifyOutgoingArcs_singleInOut(joinp);
					net.removePlace(joinp);
					removedPlaces.add(joinp);
					net.updatePlaceLabel(p, StringUtils.removeDuplicates(p.getLabel() + "," + joinp.getLabel()));
				}
				net.removeTransition(joinpostt);
			}
		}
		return removedPlaces;
	}
}
