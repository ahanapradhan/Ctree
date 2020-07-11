package petrinetmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.IUtils;

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
			} else if (places.containsKey(trg) && transitions.containsKey(src)) {
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

	public void addArc(Arc a) {
		if (!arcs.contains(a)) {
			arcs.add(a);
		}
	}

	public void removeArc(Arc a) {
		arcs.remove(a);
	}

	public void addPlace(Place p) {
		places.put(p.getLabel(), p);
		ps.add(p);
		if (p.getInArcs() != null) {
			arcs.addAll(p.getInArcs());
		}
		if (p.getOutArcs() != null) {
			arcs.addAll(p.getOutArcs());
		}
	}

	public void removePlace(Place p) {
		places.remove(p.getLabel());
		ps.remove(p);
		List<Arc> arcstoremove = new ArrayList<Arc>();
		String plabel = p.getLabel();
		for (int i = 0; i < arcs.size(); i++) {
			Arc a = arcs.get(i);
			String inlabel = null, outlabel = null;
			if (a.getInNode() != null) {
				inlabel = a.getInNode().getLabel();
			}
			if (a.getOutNode() != null) {
				outlabel = a.getOutNode().getLabel();
			}
			if (plabel.equals(inlabel) || plabel.equals(outlabel)) {
				arcstoremove.add(arcs.get(i));
			}
		}
		for (Arc a : arcstoremove) {
			arcs.remove(a);
		}
		for (Transition t : ts) {
			t.removeFromPreNodes(p);
			t.removeFromPostNodes(p);
		}
	}

	public void removeTransition(Transition t) {
		transitions.remove(t.getLabel());
		ts.remove(t);
		List<Arc> arcstoremove = new ArrayList<Arc>();
		String tlabel = t.getLabel();
		for (int i = 0; i < arcs.size(); i++) {
			Arc a = arcs.get(i);
			String inlabel = null, outlabel = null;
			if (a.getInNode() != null) {
				inlabel = a.getInNode().getLabel();
			}
			if (a.getOutNode() != null) {
				outlabel = a.getOutNode().getLabel();
			}
			if (tlabel.equals(inlabel) || tlabel.equals(outlabel)) {
				arcstoremove.add(arcs.get(i));
			}
		}
		for (Arc a : arcstoremove) {
			arcs.remove(a);
		}
		
		for (Place p : ps) {
			p.removeFromPreNodes(t);
			p.removeFromPostNodes(t);
		}
	}

	public Net() {
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
		for (Place p : ps) {
			sb.append("    \"" + p.getLabel() + "\";\n");
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
		return sb.toString();
	}

	public void updatePlaceLabel(Place p, String newLabel) {
		places.remove(p.getLabel());
		p.setLabel(newLabel);
		places.put(newLabel, p);
	}

	public static Transition haveCommonPostTrans(Set<Place> ps) {
		Set<Node> postps = new HashSet<Node>();
        Transition t = null;
		Iterator psit = ps.iterator();
		for (int i = 0; i < ps.size(); i++) {
			Place p = (Place) psit.next();
			if (i == 0) {
				postps = p.getPostNodes();
			} else {
				postps.retainAll(p.getPostNodes());
			}
		}

		for (Node n : postps) {
			/*
			 * as of now returning the 1st common transition
			 * assuming that only 1 common post transition in structured AND block as AND join.
			 * May have to change later to handle more complecated AND structure
			 */
			t = (Transition) n;
			break;
		}
		return t;
	}
	
	public Arc findArc(Node in, Node out) {
		boolean yes = false;
		for(Arc a : arcs) {
			if (in == null && a.getInNode() == null) {
				yes = yes || true;
			}
			else if (in != null && a.getInNode() != null && in == a.getInNode()) {
				yes = yes || true;
			}
			else {
				yes = yes && false;
			}
			
			if (out == null && a.getOutNode() == null) {
				yes = yes || true;
			}
			else if (out != null && a.getOutNode() != null && out == a.getOutNode()) {
				yes = yes || true;
			}
			else {
				yes = yes && false;
			}
			
			if (yes) {
				return a;
			}
		}
		return null;
	}
	
	public void replaceMultiInOutTransitions() {
		Set<Transition> toremove = new HashSet<Transition>();
		Set<Transition> toadd = new HashSet<Transition>();
		for( Transition t : ts) {
			if (t.howManyInArcs() >= IUtils.MORE_THAN_ONE
					&& t.howManyOutArcs() >= IUtils.MORE_THAN_ONE) {
				
				toremove.add(t);
				Transition t1 = new Transition(t.getLabel()+"_1");
				Transition t2 = new Transition(t.getLabel()+"_2");
				Place p = new Place("p"+t.getLabel()+"_12");
				Arc a1 = new Arc(t1, p);
				Arc a2 = new Arc(p, t2);
				for (Arc tin : t.getInArcs()) {
					tin.setOutNode(t1);
				}
				for (Arc tout : t.getOutArcs()) {
					tout.setInNode(t2);
				}
				addArc(a1);
				addArc(a2);
				ps.add(p);
				places.put(p.getLabel(), p);
				transitions.put(t1.getLabel(), t1);
				transitions.put(t2.getLabel(), t2);
				transitions.remove(t.getLabel());
				toadd.add(t1);
				toadd.add(t2);
			}
		}
		ts.removeAll(toremove);
		ts.addAll(toadd);
	}
}
