package petrinetmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.IUtils;

public abstract class Node {
    String label;
    List<Arc> inArcs = null;
	List<Arc> outArcs = null;
	
	Set<Node> preNodes = null;
	Set<Node> postNodes = null;
	
	protected int unvisitedInArcs; // this is only for ecws
	
	protected abstract String getFork();
	protected abstract String getJoin();
	
	/**
	 * This method returns true if the node has Matching number of in and out arcs
	 * @param inNum number of input arcs to match
	 * @param outNum number of outgoing arcs
	 * @return true or false
	 * 
	 * in/out Num: if 0, match with 0. if 1, match with 1. if any other value, match with > 1
	 * e.g. inNum 1, outNum 2: does this node has 1 input arc and multiple output arcs?
	 *      inNum 1, outNum 1: does this node has 1 input arc and 1 outgoing arc?
	 */

	protected boolean hasMatchingInOut(int inNum, int outNum) {
		boolean inyes, outyes;
		boolean ingt, outgt;
		ingt = (inNum > IUtils.ONE) ? true : false;
		outgt = (outNum > IUtils.ONE) ? true : false;
		
		if (getInArcs() == null || getOutArcs() == null) {
			return false;
		} 
        
		inyes = (ingt) ? (inArcs.size() > IUtils.ONE) : (inArcs.size() == inNum);
		outyes = (outgt) ? (outArcs.size() > IUtils.ONE) : (outArcs.size() == outNum);
		
		return (inyes && outyes);
	}

	public int howManyInArcs() {
		int n = 0;
		if (inArcs != null) {
			n = inArcs.size();
		}
		return n;
	}

	public int howManyOutArcs() {
		int n = 0;
		if (outArcs != null) {
			n = outArcs.size();
		}
		return n;
	}

	protected Set<Node> getPreNodes() {
		if (inArcs != null && preNodes == null) {
			preNodes = new HashSet<Node>();
			
			for(Arc a : inArcs) {
				Node n = a.getInNode();
				if (n != null) {
					preNodes.add(n);
				}
			}
		}
		return preNodes;
	}
	
	protected Set<Node> getPostNodes() {
		if (outArcs != null && (postNodes == null || postNodes.isEmpty())) {
			postNodes = new HashSet<Node>();
			for (Arc a : outArcs) {
				Node n = a.getOutNode();
				if (n != null) {
					postNodes.add(n);
				}
			}
		}
		return postNodes;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String s) {
		label = s;
	}

	public Node(String s) {
		label = s;
		unvisitedInArcs = 0;
    }
    
    public void addInArc(Arc a) {
    	if (inArcs == null) {
    		inArcs = new ArrayList<Arc>();
    	}
    	inArcs.add(a);
    	unvisitedInArcs++;
    }
    
    public void removeOutArc(Arc a) {
    	if(outArcs != null) {
    		outArcs.remove(a);
    	}
    	postNodes = null; // have to rebuild on next call
    }
    
    public void addOutArc(Arc a) {
    	if (outArcs == null) {
    		outArcs = new ArrayList<Arc>();
    	}
    	outArcs.add(a);
    	postNodes = null; // have to rebuild on next call
    }
    
    public List<Arc> getInArcs() {
		return inArcs;
	}

	public List<Arc> getOutArcs() {
		return outArcs;
	}
	
	public void removeFromPreNodes(Node n) {
		if (getPreNodes() == null || preNodes.isEmpty()) {
			return;
		}
		Set<Arc> preArcsToRemove = new HashSet<Arc>();
		for(Arc a : inArcs) {
			Node pren = a.getInNode();
			if(pren.getLabel().equals(n.getLabel())) {
				preArcsToRemove.add(a);
			}
		}
		inArcs.removeAll(preArcsToRemove);
	}
	
	public void removeFromPostNodes(Node n) {
		if (getPostNodes() == null || postNodes.isEmpty()) {
			return;
		}
		Set<Arc> postArcsToRemove = new HashSet<Arc>();
		for(Arc a : outArcs) {
			Node postn = a.getInNode();
			if(postn.getLabel().equals(n.getLabel())) {
				postArcsToRemove.add(a);
			}
		}
		outArcs.removeAll(postArcsToRemove);
	}
}
