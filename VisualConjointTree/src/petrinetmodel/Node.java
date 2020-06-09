package petrinetmodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Node {
	
	String ecws = null;
    String label;
    List<Arc> inArcs = null;
	List<Arc> outArcs = null;
	
	Set<Node> preNodes = null;
	Set<Node> postNodes = null;
	
	protected int unvisitedInArcs;
	
	protected abstract String getOpen();
	protected abstract String getClose();
	
	public Set<Node> getPreNodes() {
		if (inArcs != null && preNodes == null) {
			preNodes = new HashSet<Node>();
			
			for(Arc a : inArcs) {
				Node n = a.getInNode();
				if (n!=null && !preNodes.contains(n)) {
					preNodes.add(n);
					if (n.getPreNodes() != null) {
					    preNodes.addAll(n.getPreNodes());
					}
				}
			}
		}
		return preNodes;
	}
	
	public Set<Node> getPostNodes() {
		if (outArcs != null && postNodes == null) {
			postNodes = new HashSet<Node>();
			
			for(Arc a : outArcs) {
				Node n = a.getOutNode();
				if (n!=null && !postNodes.contains(n)) {
					postNodes.add(n);
					if (n.getPostNodes() != null) {
					    postNodes.addAll(n.getPostNodes());
					}
				}
			}
		}
		return postNodes;
	}
    
    public String getLabel() {
    	return label;
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
    
    public void addOutArc(Arc a) {
    	if (outArcs == null) {
    		outArcs = new ArrayList<Arc>();
    	}
    	outArcs.add(a);
    }
    
    public List<Arc> getInArcs() {
		return inArcs;
	}

	public List<Arc> getOutArcs() {
		return outArcs;
	}

	public String getECWS() {
		if (ecws == null) {
			ecws = buildECWS();
		}
		return ecws;
	}

	public String buildECWS() {
		StringBuilder sb = new StringBuilder();
		if (inArcs != null) {
			handleInArcs(sb);
		}
		else {
			sb.append(this.getLabel() + " ");
		}
		if (outArcs != null) {
		    handleOutArcs(sb);
		}
		return sb.toString();
	}
	private void handleOutArcs(StringBuilder sb) {
		if (unvisitedInArcs == 0) {
			if (outArcs.size() == 1) {
				sb.append(outArcs.get(0).getOutNode().buildECWS());
			} 
			else if (outArcs.size() > 1) {
				for (Arc a : outArcs) {
					sb.append(getOpen());
					sb.append(a.getOutNode().buildECWS());
				}
			}
		}
	}
	private void handleInArcs(StringBuilder sb) {
		if (inArcs.size() > 1 && unvisitedInArcs > 1) {
			sb.append(getClose());
			unvisitedInArcs--;
		}
		else if(inArcs.size() > 1 && unvisitedInArcs == 1) {
			sb.append(getClose() + this.getLabel() + " ");
			unvisitedInArcs--;
		}
		
		if (inArcs.size() == 1) {
			sb.append(this.getLabel() + " ");
			unvisitedInArcs--;
		}
	}
}
