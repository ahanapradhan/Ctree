package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import petrinetmodel.FoldedPlace;
import petrinetmodel.Net;
import petrinetmodel.Place;
import util.IUtils;

public class NetFolder {

	public static Net foldLabelOfPlace(Net n, Place p) {
		Net m = new Net(n);
		Set<Place> ps = m.getPlaces();
		for (Place e : ps) {
			if (e.getLabel().equals(p.getLabel())) {
				FoldedPlace ep = new FoldedPlace(e);
				ep.getFoldedLabel();
			}
		}
		return m;
	}

	/**
	 * folds a given place in a net that has single in-out post-transition
	 * 
	 * @param net the net in context
	 * @param p   the place to fold
	 * @return the set of places which got folded. They are now non-existent in the
	 *         net.
	 */
	public static void foldSingleInOutPostTransitions(Net net, Place p) {
		if (p.howManyOutArcs() == IUtils.ZERO) {
			return;
		}
		Set<Place> ps = net.getPlaces();
		FoldedPlace ep = null;
		Place e = null;
		Iterator psit = ps.iterator();
		while (psit.hasNext()) {
			e = (Place) psit.next();
			if (e.getLabel().equals(p.getLabel())) {
				ep = new FoldedPlace(e);
				ep.foldSingleInOutPostTransitions(net);
				break;
			}
		}
	}

	/**
	 * folds the whole net whenever possible for any place 
	 * that has single in-out post transitions
	 * @param net
	 */
	public static void foldOneInOneOutTransitions(Net net) {
		Queue<Place> places = new LinkedList<Place>(net.getPlaces());
		Place p = null;
		do {
			p = places.poll();
			if (p != null) {
				foldSingleInOutPostTransitions(net, p);
				places.add(p);
			}
			
		} while (!p.getLabel().equals(places.peek().getLabel()));
	}

	public static Net foldLabelOfSourcePlace(Net n) {
		Net m = new Net(n);
		Set<Place> ps = m.getPlaces();
		for (Place e : ps) {
			if (e.howManyInArcs() == IUtils.ZERO) { // source place
				FoldedPlace ep = new FoldedPlace(e);
				ep.getFoldedLabel();
			}
		}
		return m;
	}
}
