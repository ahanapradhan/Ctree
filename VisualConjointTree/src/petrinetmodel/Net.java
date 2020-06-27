package petrinetmodel;

import java.util.ArrayList;
import java.util.HashMap;
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
	String name;

	public Net(Net other) { 
		name = other.getName();		
		places = new HashMap<String,Place>();
		transitions = new HashMap<String, Transition>();
		ps = new HashSet<Place>();
		ts = new HashSet<Transition>();
		arcs = new ArrayList<Arc>();
		
		for (Place otherp : other.ps) {
			Place myp = new Place(otherp.getLabel());
			places.put(otherp.getLabel(), myp);
			ps.add(myp);
		}
		
		for (Transition othert : other.ts) {
			Transition myt = new Transition(othert.getLabel());
			transitions.put(othert.getLabel(),myt);
			ts.add(myt);
		}
		
		for (Arc othera : other.arcs) {
			Place myp;
			Transition myt;
			String src = othera.getInNode().getLabel();
			String trg = othera.getOutNode().getLabel();
			Arc mya = null;
			if (places.containsKey(src) && transitions.containsKey(trg)) {
				myp = places.get(src);
				myt = transitions.get(trg);
				mya = new Arc(myp,myt);
				myp.addOutArc(mya);
				myt.addInArc(mya);
			} 
			else if (places.containsKey(trg) && transitions.containsKey(src)) {
				myp = places.get(trg);
				myt = transitions.get(src);
				mya = new Arc(myt,myp);
				myt.addOutArc(mya);
				myp.addInArc(mya);
			}
			if (mya !=  null) { // may be null check not needed if everything is proper
				arcs.add(mya);
			}			
		}
	}
	
	public Net()
	{		
	}
	
	public void setName(String n) {
		name = n;
	}

	public String getName() {
		return name;
	}

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
		getPlaces();  // to initialize ps;
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
		getTransitions(); // to initialize ts
	}
	
	public void addArcs(List<Arc> as) {
		arcs = as;
	}
	
	public List<Arc> getArcs(){
		return arcs;
	}
	
	public String printNetForDot() {
		StringBuilder sb = new StringBuilder();
		// sb.append("\n```dot\n");
		sb.append("digraph N {\n    label=\"" + name + "\";\n");
		sb.append("    rankdir=\"LR\";\n");
		sb.append("    subgraph place {\n");
		sb.append("    graph [shape=circle];\n");
		sb.append("    node [shape=circle,fixedsize=true,width=0.7, style=filled, color=darkseagreen2];\n");
	    for(Place p : ps) {
	    	sb.append("    "+ p.getLabel()+";\n");
	    }
		sb.append("    }\n");
		sb.append("    subgraph transitions {\n");
		sb.append("    node [shape=rect,height=0.7,width=0.2, style=filled,color=\".7 .3 1.0\"];\n");
		for(Transition t : ts) {
	    	sb.append("    "+ t.getLabel()+";\n");
	    }  	        
	    sb.append("    }\n");
		for (Arc a : arcs) {
				sb.append("    \"" + a.getInNode().getLabel() + "\" -> \"" + a.getOutNode().getLabel() + "\";\n");
		}
		sb.append("}\n");
		//sb.append("```\n");
		return sb.toString();
	}

	public String printNetForDot(Set<String> cr) {
		StringBuilder sb = new StringBuilder();
		// sb.append("\n```dot\n");
		sb.append("digraph N {\n    label=\"" + name + "\";\n");
		sb.append("    rankdir=\"LR\";\n");
		sb.append("    subgraph place {\n");
		sb.append("    graph [shape=circle];\n");
		sb.append("    node [shape=circle,fixedsize=true,width=0.7, style=filled, color=darkseagreen2];\n");
		for (Place p : ps) {
			sb.append("    " + p.getLabel());
			if (cr.contains(p.getLabel())) {
				sb.append(" [color=crimson]");
			}
			sb.append(";\n");
		}
		sb.append("    }\n");
		sb.append("    subgraph transitions {\n");
		sb.append("    node [shape=rect,height=0.7,width=0.2, style=filled,color=\".7 .3 1.0\"];\n");
		for (Transition t : ts) {
			sb.append("    " + t.getLabel() + ";\n");
		}
		sb.append("    }\n");
		for (Arc a : arcs) {
			sb.append("    \"" + a.getInNode().getLabel() + "\" -> \"" + a.getOutNode().getLabel() + "\";\n");
		}
		sb.append("}\n");
		// sb.append("```\n");
		return sb.toString();
	}

	public String getECWS() {
		StringBuilder sb = new StringBuilder();
		
		for(Place p : ps) {
			if (p.getInArcs() == null) {
				// source place
				sb.append(p.getECWS());				
			}
		}	
		return sb.toString();
	}
}
