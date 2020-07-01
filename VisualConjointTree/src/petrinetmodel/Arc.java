package petrinetmodel;

public class Arc {
	
	Node in;
	Node out;
	String label;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Node getInNode() {
		return in;
	}

	public Node getOutNode() {
		return out;
	}
	
	public Arc(Node i, Node o) {
		in = i;
		out = o;
	}
	
	public void setInNode(Node i) {
		in = i;
	}
	public void setOutNode(Node o) {
		out = o;
	}

}
