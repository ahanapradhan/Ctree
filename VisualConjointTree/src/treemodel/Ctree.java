package treemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.IUtils;

public class Ctree {
	String label;
	Set<AbstractTreeNode> nodes;
	CNode root;

	public Ctree() {
		label = IUtils.ORIGINAL_CTREE_NAME;
		nodes = null;
		root = null;
	}

	public Ctree(Ctree c) {
		label = "Copy";
		Set<AbstractTreeNode> ns = new HashSet<AbstractTreeNode>();
		for (AbstractTreeNode n : c.nodes) {
			if (n instanceof CNode) {
				CNode cn = new CNode((CNode) n);
				ns.add(cn);
			} else {
				ANode an = new ANode((ANode) n);
				ns.add(an);
			}
		}
		nodes = new HashSet<AbstractTreeNode>(ns);
		root = new CNode(c.root);
	}

	public void setName(String n) {
		label = n;
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

	public int howManyNodes() {
		return nodes.size();
	}
	
	public String printCtreeForDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n    label=\"" + label + "\";\n");
		for (AbstractTreeNode n : nodes) {
			if (n.getParent() != -1) {
				sb.append("    " + n.getParent() + " -> " + n.getId() + ";\n");
				sb.append("    " + n.getParent() + " [label=\"" + getNodebyId(n.getParent()).getVisLabel() + "\"];\n");
				sb.append("    " + n.getId() + " [label=\"" + n.getVisLabel() + "\"];\n");
			} else {
				sb.append("    " + n.getId() + ";\n");
				sb.append("    " + n.getId() + " [label=\"" + n.getVisLabel() + "\"];\n");
			}
			if (n instanceof ANode) {
				sb.append("    " + n.getId() + " [shape=box,style=filled,color=lightcoral,label=" + n.getVisLabel()
						+ "];\n");
			}
		}
		sb.append("}\n");
		return sb.toString();
	}

	public Set<AbstractTreeNode> getNodes() {
		return nodes;
	}

	public String getName() {
		return label;
	}

	public void removeSubtreeOf(AbstractTreeNode abstractTreeNode) { // including cn itself
		AbstractTreeNode pn = getNodebyId(abstractTreeNode.getParent());
		if (pn != null) {
			pn.removeChild(abstractTreeNode.getId());
		}
		Set<AbstractTreeNode> subtreeNodes = getSubtreeNodes(abstractTreeNode);
		nodes.removeAll(subtreeNodes);
	}

	private Set<AbstractTreeNode> getSubtreeNodes(AbstractTreeNode n) {
		Set<AbstractTreeNode> ret = new HashSet<AbstractTreeNode>();
		List<Integer> childern = n.getChildrenIds();
		if (childern != null) {
			for (Integer ch : childern) {
				ret.addAll(getSubtreeNodes(getNodebyId(ch)));
			}
		}
		ret.add(n);
		return ret;
	}

	public void clearTillRoot(AbstractTreeNode cn) {
		// cn has been deleted from tree
		// remove/clear only the ancestors
		//start from parent of cn
		AbstractTreeNode pn = getNodebyId(cn.getParent());
		
		while (pn != null) {
			pn.removeAllPlaces();
			if (pn instanceof CNode) {
				List<Integer> allch = new ArrayList<>(pn.getChildrenIds());
			    pn.removeOtherChildrenButMe(cn);
			    List<Integer> me = new ArrayList<>(pn.getChildrenIds());
			    allch.removeAll(me);
			    for(Integer i : allch) {
			    	removeSubtreeOf(getNodebyId(i));
			    }
			}
			cn = pn;
			pn = getNodebyId(pn.getParent());
		}
		// clear the line of ancestors
		
	}

	public void deletePlace(String p) {
		for (AbstractTreeNode n : nodes) {
			if (n instanceof CNode) {
				CNode cn = (CNode) n;
				if (cn.doesItHaveIt(p)) {
					cn.removePlace(p);
					break;
				}
			}
		}
	}
	
	protected void deletePlaces(String[] ps) {
		for (AbstractTreeNode n : nodes) {
			if (n instanceof CNode) {
				CNode cn = (CNode) n;
				cn.removePlaces(new HashSet<String>(Arrays.asList(ps)));
			}
		}
	}
	
	protected boolean isDysfunctionalTree() {
		return false;
	}
	
	protected boolean isBreakOffSet(String[] ps) {
		deletePlaces(ps);
		return isDysfunctionalTree();
	}

}
