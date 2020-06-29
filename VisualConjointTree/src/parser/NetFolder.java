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

	public static Set<Place> foldPlace(Net net, Place p) {
		if (p.howManyOutArcs() == IUtils.ZERO) {
			return null;
		}
		Set<Place> ps = net.getPlaces();
		Set<Place> removeplace = null;
		FoldedPlace ep = null;
		Place e = null;
		Iterator psit = ps.iterator();
		while (psit.hasNext()) {
			e = (Place) psit.next();
			if (e.getLabel().equals(p.getLabel())) {
				ep = new FoldedPlace(e);
				removeplace = ep.foldPostNodes();
				break;
			}
		}
		for (Place rp : removeplace) {
			net.removePlace(rp);
		}
		return removeplace;
	}

	public static void foldSeqPlaces(Net net) {
		Queue<Place> places = new LinkedList<Place>(net.getPlaces());
		Place p = null;
		do {
			p = places.poll();
			//System.out.println("going to fold " + p.getLabel());
			if (p != null) {
				Set<Place> rp = foldPlace(net, p);
				//System.out.println("folded " + p.getLabel());
				if (rp != null) {
					for (Place rps : rp) {
						places.remove(rps);
					}
				}
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
