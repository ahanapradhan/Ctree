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
	 * @param net       the net in context
	 * @param p         the place to fold
	 * @param postTFlag indicator of if post transition is single-in-out or
	 *                  single-in-multi-out
	 * @return the set of places which got folded. They are now non-existent in the
	 *         net.
	 */
	public static Set<Place> foldPostTransitions(Net net, Place p, int postTFlag) {
		if (p.howManyOutArcs() == IUtils.ZERO) {
			return null;
		}
		Set<Place> foldedPlaces = null;
		FoldedPlace ep = null;
		ep = new FoldedPlace(p);
		switch (postTFlag) {
		case IUtils.SINGLE_IN_OUT:
			foldedPlaces = ep.foldSingleInOutPostTransitions(net);
			break;
		case IUtils.SINGLE_IN_MULTI_OUT:
			foldedPlaces = ep.foldSingleInMultiOutPostTransitions(net);
			break;
		}
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
				foldedPlaces = foldPostTransitions(net, p, IUtils.SINGLE_IN_OUT);
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
		Queue<Place> unfoldedPlaces = new LinkedList<Place>();
		Queue<Place> places = new LinkedList<Place>(net.getPlaces());
		Set<Place> foldedPlaces = null;
		Place p = null;
		int counter = places.size();
		do {
			p = places.poll();
			counter--;
			unfoldedPlaces.add(p);
			if (p != null) {
				foldedPlaces = foldPostTransitions(net, p, IUtils.SINGLE_IN_MULTI_OUT);
				places.add(p); // either it has been folded, or not yet (may need to try later again)

				if (foldedPlaces != null && !foldedPlaces.isEmpty()) {
					places.removeAll(foldedPlaces);
					unfoldedPlaces.addAll(foldedPlaces);
					counter -= foldedPlaces.size();
				}
			}
			if (counter <= IUtils.ZERO) {
				/*
				 * ideally it should be == 0. but somehow, when test-cases run in loop, this is
				 * causing integer underflow. making it < solves this problem. Have to reason
				 * later.
				 */

				if (places.size() == unfoldedPlaces.size()) {
					// one iteration complete
					break;
				} else {
					/*
					 * again have to go one iteration
					 */
					counter = places.size();
					unfoldedPlaces.clear();
				}
			}
			// System.out.println("counter " + counter+" still going on...");
		} while (true);
	}
	
	public static void fold(Net net) {
		//
		foldOneInMultiOutTransitions(net);
		foldOneInOneOutTransitions(net);
	}
}
