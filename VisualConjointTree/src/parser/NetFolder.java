package parser;

import java.util.Set;

import petrinetmodel.FoldedPlace;
import petrinetmodel.Net;
import petrinetmodel.Place;
import util.IUtils;

public class NetFolder {

	public static Net fold(Net n, Place p) {
		Net m = new Net(n);
		Set<Place> ps = m.getPlaces();
		for (Place e : ps) {
			if (e.getLabel().equals(p.getLabel())) {
				FoldedPlace ep = new FoldedPlace(e);
				ep.getFolding();
			}
		}
		return m;
	}
	
	public static Net foldWholeNet(Net n) {
		Net m = new Net(n);
		Set<Place> ps = m.getPlaces();
		for (Place e : ps) {
			if (e.howManyInArcs() == IUtils.ZERO) { // source place
				FoldedPlace ep = new FoldedPlace(e);
				ep.getFolding();
			}
		}
		return m;
	}
}
