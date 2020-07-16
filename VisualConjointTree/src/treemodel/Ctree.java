package treemodel;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
		label = "Copy of " + c.label;
		nodes = new HashSet<AbstractTreeNode>();
		for (AbstractTreeNode n : c.nodes) {
			if (n instanceof CNode) {
				CNode cn = new CNode((CNode) n);
				nodes.add(cn);
				if (cn.getId() == c.root.getId()) {
					root = cn;
				}
			} else {
				ANode an = new ANode((ANode) n);
				nodes.add(an);
			}
		}
	}

	public void setName(String n) {
		label = "C-tree of " + n;
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

	public int howManyNodes() {
		return nodes.size();
	}
	
	public String printCtreeForDot() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n    label=\"" + label + "\";\n");
		for (AbstractTreeNode n : nodes) {
			if (n.getParentIds() != null) {
				for (int pid : n.getParentIds()) {
					sb.append("    " + pid + " -> " + n.getId() + ";\n");
					sb.append("    " + pid + " [label=\"" + getNodebyId(pid).getVisLabel() + "\"];\n");
					sb.append("    " + n.getId() + " [label=\"" + n.getVisLabel() + "\"];\n");
				}
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
	
	protected void cleanGCS() {
		// delete the nodes which dont have any place in post-nodes
		//this is for memory efficiency
	}

	private Set<Integer> getAncestors(Integer nodeid) {
		Set<Integer> ancestors = new HashSet<Integer>();
		AbstractTreeNode node = getNodebyId(nodeid);
		if (node.getParentIds() != null) {
			ancestors.addAll(node.getParentIds());
		}
		Set<Integer> others = new HashSet<Integer>();
		for (int i : ancestors) {
			others.addAll(getAncestors(i));
		}
		ancestors.addAll(others);
		return ancestors;
	}

	private Set<Integer> getDescendants(Integer nodeid) {
		Set<Integer> descandants = new HashSet<Integer>();
		AbstractTreeNode node = getNodebyId(nodeid);
		if (node.getChildrenIds() != null) {
			descandants.addAll(node.getChildrenIds());
		}
		Set<Integer> others = new HashSet<Integer>();
		for (int i : descandants) {
			others.addAll(getDescendants(i));
		}
		descandants.addAll(others);
		return descandants;
	}

	protected void removeNonConcurrentPlaces(AbstractTreeNode node) {
		node.removeAllPlaces();
		Set<Integer> ancestors = getAncestors(node.getId());
		Set<Integer> descendants = getDescendants(node.getId());
		Set<Integer> others = new HashSet<Integer>();
		for (int i : ancestors) {
			if (getNodebyId(i) instanceof CNode) {
				Set<Integer> d = new HashSet<Integer>(getNodebyId(i).getChildrenIds());
				d.removeAll(ancestors);
				for(Integer k : d) {
					others.addAll(getDescendants(k));
				}
				others.addAll(d);
			}
		}
		ancestors.addAll(descendants);
		ancestors.addAll(others);
		for (int i : ancestors) {
			AbstractTreeNode n = getNodebyId(i);
			n.removeAllPlaces();
		}
	}

	protected void deletePlaces(Set<String> ps) {
		for (AbstractTreeNode n : nodes) {
			if (n instanceof CNode) {
				CNode cn = (CNode) n;
				cn.removePlaces(ps);
			}
		}
		root.removePlaces(ps);
	}
	
	protected boolean isDysfunctionalTree() {
		
		boolean rootempty = root.isThereNoPlace();
		boolean reachLeaf = false;
		Queue<AbstractTreeNode> q = new LinkedList<AbstractTreeNode>();
		q.add(root);
		while (!q.isEmpty()) {
			AbstractTreeNode n = q.poll();
			List<Integer> ch = n.getChildrenIds();
			if (ch == null || ch.size() == IUtils.ZERO) {
				reachLeaf = true;
				break;
			}
			for (Integer i : ch) {
				AbstractTreeNode chn = getNodebyId(i);
				if (chn.isThereNoPlace()) {
					q.add(chn);
				}
			}
		}
		return rootempty && reachLeaf;
	}

	protected boolean isBreakOffSet(Set<String> ps) {
		deletePlaces(ps);
		return isDysfunctionalTree();
	}

	public Set<String> getAllPlaces() {
		Set<String> places = new HashSet<String>();
		for (AbstractTreeNode n : nodes) {
			if (n instanceof CNode) {
				places.addAll(((CNode) n).getPlaces());
			}
		}
		return places;
	}
	

	private boolean doesHaveMPE(ANode big, ANode small, Ctree smtree) {
		if (big.howManyChildren() != small.howManyChildren()) {
			return false;
		}
		// here, null or 0 children impossible
		Set<AbstractTreeNode> bigAnds = getChildren(big);
		Set<AbstractTreeNode> smallAnds = smtree.getChildren(small);
		int MPEcount = IUtils.ZERO;
		// both not null
		for (AbstractTreeNode sn : smallAnds) {
			for(AbstractTreeNode bn : bigAnds) {
				if (doesHaveMPE((CNode)bn, (CNode)sn, smtree)) {
					MPEcount++;
					break;
				}
			}
		}
		return (MPEcount == smallAnds.size());
	}
	
	private boolean doesHaveMPE(CNode big, CNode small, Ctree smtree) {
		if (!big.isSubset(small)) {
			return false;
		}
		Set<AbstractTreeNode> bigAnds = getChildren(big);
		Set<AbstractTreeNode> smallAnds = smtree.getChildren(small);
		if (bigAnds == null) {
			return (smallAnds == null ? true : false);
		} else {
			if (smallAnds == null) {
				return true;
			}
		}
		int MPEcount = IUtils.ZERO;
		// both not null
		for (AbstractTreeNode sn : smallAnds) {
			for(AbstractTreeNode bn : bigAnds) {
				if (doesHaveMPE((ANode)bn, (ANode)sn, smtree)) {
					MPEcount++;
					break;
				}
			}
		}
		return (MPEcount == smallAnds.size());
	}

	private Set<AbstractTreeNode> getChildren(AbstractTreeNode n) {
		if (n.howManyChildren() == IUtils.ZERO) {
			return null;
		}
		Set<AbstractTreeNode> set = new HashSet<AbstractTreeNode>();
		for (Integer i : n.getChildrenIds()) {
			set.add(getNodebyId(i));
		}
		return set;
	}

	protected boolean doesHaveMPE(Ctree small) {
		return doesHaveMPE((CNode) root, (CNode) small.getRoot(), small);
	}

}
