package treemodel;

import java.util.HashSet;
import java.util.Set;

public class CNode extends AbstractTreeNode {

	Set<String> places;
	String label;

	public CNode() {
		super();
		places = null;
		label = null;
	}
	
	public CNode(CNode n) { // only used for copy construction
		super(n);
		places = new HashSet<String>(n.getPlaces());
		label = null;
	}

	public void addPlace(String p) {
		if (places == null) {
			places = new HashSet<String>();
		}
		places.add(p);
	}

	public void addPlaces(Set<String> sp) {
		for (String p : sp) {
			addPlace(p);
		}
	}
	
	public void removePlaces(Set<String> sp) {
		places.removeAll(sp);
		label = null; // has to form new label later when needed
	}
	
	public void removePlace(String p) {
		places.remove(p);
		label = null; // has to form new label later when needed
	}

	public Set<String> getPlaces() {
		return places;
	}

	public String getLabel() {
		if (label == null) {
			StringBuilder builder = new StringBuilder();
			for (String s : places) {
				builder.append(s);
			}
			label = builder.toString();
		}
		return label;
	}

	public String getVisLabel() {
		String prefix = "";
		StringBuilder builder = new StringBuilder();
		for (String s : places) {
			builder.append(prefix);
			prefix = ",";
			builder.append(s);
		}
		return builder.toString();
	}
	
	public boolean doesItHaveIt(String p) {
		return places.contains(p);
	}

	@Override
	public void removeAllPlaces() {
		places.clear();
		label = null;		
	}

}
