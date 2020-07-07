package parser;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import petrinetmodel.FoldedPlace;
import petrinetmodel.Net;
import petrinetmodel.Place;
import util.IUtils;

public interface NetFolder {

	/**
	 * folds a given place in a net that has single in-out post-transition
	 * 
	 * @param net the net in context
	 * @param p   the place to fold
	 * @return the set of places which got folded. They are now non-existent in the
	 *         net.
	 */
	public static Set<Place> foldSingleInOutPostTransitions(Net net, Place p) {
		if (p.howManyOutArcs() == IUtils.ZERO) {
			return null;
		}
		Set<Place> foldedPlaces = null;
		FoldedPlace ep = null;
		ep = new FoldedPlace(p);
		foldedPlaces = ep.foldSingleInOutPostTransitions(net);

		return foldedPlaces;
	}

	/**
	 * folds the whole net whenever possible for any place that has single in-out
	 * post transitions
	 * 
	 * @param net
	 */
	public static void foldOneInOneOutTransitions(Net net) {
		Queue<Place> places = new LinkedList<Place>(net.getPlaces());
		Set<Place> foldedPlaces = null;
		Place p = null;
		String qFrontLabel = "";
		do {
			p = places.poll();
			String oldpLabel = p.getLabel();
			if (p != null) {
				foldedPlaces = foldSingleInOutPostTransitions(net, p);
				if (foldedPlaces != null && !foldedPlaces.isEmpty()) {
					places.removeAll(foldedPlaces);
				}
				if (!oldpLabel.equals(p.getLabel())) {
					places.add(p);
				}
			}
			if (places.peek() != null) {
				qFrontLabel = places.peek().getLabel();
			}
		} while (!p.getLabel().equals(qFrontLabel));
	}

	/**
	 * whenever a place has AND-forking as post transition, fold the whole
	 * structured AND basic block using this function
	 * 
	 * @param net
	 */
	public static void foldOneInMultiOutTransitions(Net net) {
		Queue<Place> places = new LinkedList<Place>(net.getPlaces());
		Set<Place> foldedPlaces = null;
		Place p = null;
		String qFrontLabel = "";
		do {
			p = places.poll();
			String oldpLabel = p.getLabel();
			if (p != null) {
				foldedPlaces = foldSingleInMultiOutPostTransitions(net, p);
				if (foldedPlaces == null) {
					places.add(p); // need to try folding again after sometime
				}
				if (foldedPlaces != null && !foldedPlaces.isEmpty()) {
					places.removeAll(foldedPlaces);
				}
				if (!oldpLabel.equals(p.getLabel())) {
					places.add(p);
				}
			}
			if (places.peek() != null) {
				qFrontLabel = places.peek().getLabel();
			}
		} while (!p.getLabel().equals(qFrontLabel));

	}

	public static Set<Place> foldSingleInMultiOutPostTransitions(Net net, Place p) {
		if (p.howManyOutArcs() == IUtils.ZERO) {
			return null;
		}
		Set<Place> foldedPlaces = null;
		FoldedPlace ep = null;

		ep = new FoldedPlace(p);
		foldedPlaces = ep.foldSingleInMultiOutPostTransitions(net);

		return foldedPlaces;
	}
}
