package treemodel;

import java.util.HashSet;
import java.util.Set;

public class Ctree {
	Set<AbstractTreeNode> nodes;
	CNode root;

	public Ctree() {
		nodes = null;
		root = null;
	}

	public CNode getRoot() {
		return root;
	}

	public void addNode(AbstractTreeNode n) {
		if (nodes == null) {
			nodes = new HashSet<AbstractTreeNode>();
		}
		nodes.add(n);
	}

	public void setRoot(CNode r) {
		root = r;
		if (nodes == null) {
			addNode(r);
		} else {
			if (getNodebyId(r.getId()) == null) {
				addNode(r);
			}
		}
	}

	private AbstractTreeNode getNodebyId(int id) {
		for (AbstractTreeNode n : nodes) {
			if (id == n.getId()) {
				return n;
			}
		}
		return null;
	}
	
	public AbstractTreeNode getParentNode(AbstractTreeNode n) {
		if (n == null) {
			return null;
		}
		return getNodebyId(n.getParent());
	}

	public void printCtree() {
		System.out.println("root: " + root.getId());
		for (AbstractTreeNode n : nodes) {
			System.out.println(n.getId() + " : " + n.getLabel() + " parent id: " + n.getParent() + " children: "
					+ n.getChildrenIds());
		}
	}

	public int howManyNodes() {
		return nodes.size();
	}
	
	public String printCtreeForDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		for (AbstractTreeNode n : nodes) {
			if (n.getParent() != -1) {
				sb.append("    " + getNodebyId(n.getParent()).getLabel() + " -> " + n.getLabel() + ";\n");
			}
		}
		sb.append("}\n");
		return sb.toString();
	}

}
