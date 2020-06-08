package petrinetmodel;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    String label;
    List<Arc> inArcs = null;
	List<Arc> outArcs = null;
    
    public String getLabel() {
    	return label;
    }
    
    public Node(String s) {
        label = s;
    }
    
    public void addInArc(Arc a) {
    	if (inArcs == null) {
    		inArcs = new ArrayList<Arc>();
    	}
    	inArcs.add(a);
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
}
