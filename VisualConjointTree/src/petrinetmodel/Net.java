package petrinetmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Net {
	Map<String, Place> places = null;           // labels are key
	Map<String, Transition> transitions = null; // labels are key
	Set<Place> ps = null;
	Set<Transition> ts = null;
	List<Arc> arcs = null;
	
	public Set<Place> getPlaces() {
		if (ps == null || places.size() != ps.size()) {
		    ps = new HashSet<Place>();
		    for (Map.Entry<String, Place> e : places.entrySet()) {
			     ps.add(e.getValue());
		    }
		}
		return ps;
	}
	public void setPlaces(Map<String, Place> places) {
		this.places = places;
	}
	public Set<Transition> getTransitions() {
		if (ts == null || transitions.size() != ts.size()) {
		    ts = new HashSet<Transition>();
		    for (Map.Entry<String, Transition> e : transitions.entrySet()) {
			    ts.add(e.getValue());
		    }
		}
		return ts;
	}
	public void setTransitions(Map<String, Transition> transitions) {
		this.transitions = transitions;
	}
	
	public void addArcs(List<Arc> as) {
		arcs = as;
	}
	
	public List<Arc> getArcs(){
		return arcs;
	}
	
	public String printNetForDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		sb.append("    rankdir=\"LR\";\n");
		sb.append("    subgraph place {\n");
		sb.append("    graph [shape=circle];\n");
		sb.append("    node [shape=circle,fixedsize=true,width=0.7, style=filled, color=darkseagreen2];\n");
	    for(Place p : getPlaces()) {
	    	sb.append("    "+ p.getLabel()+";\n");
	    }
		sb.append("    }\n");
		sb.append("    subgraph transitions {\n");
		sb.append("    node [shape=rect,height=0.7,width=0.2, style=filled,color=\".7 .3 1.0\"];\n");
		for(Transition t : getTransitions()) {
	    	sb.append("    "+ t.getLabel()+";\n");
	    }  	        
	    sb.append("    }\n");
		for (Arc a : arcs) {
				sb.append("    \"" + a.getInNode().getLabel() + "\" -> \"" + a.getOutNode().getLabel() + "\";\n");
		}
		sb.append("}\n");
		return sb.toString();
	}


}
