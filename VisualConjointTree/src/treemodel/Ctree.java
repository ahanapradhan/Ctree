package treemodel;

import java.util.ArrayList;
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

	/**
	 * Have to fix this method
	 * One node may have multiple parents. Have to remove itself from each of them.
	 * 
	 * @param abstractTreeNode
	 */
	protected void removeSubtreeOf(AbstractTreeNode abstractTreeNode) { // including cn itself
		List<Integer> pids = abstractTreeNode.getParentIds();
		if (pids != null && !pids.isEmpty()) {
			for (int pid : pids) {
				AbstractTreeNode pn = getNodebyId(pid);
				if (pn != null) {
					pn.removeChild(abstractTreeNode.getId());
				}
			}
		}
		Set<AbstractTreeNode> subtreeNodes = getSubtreeNodes(abstractTreeNode);
		for(AbstractTreeNode absn : subtreeNodes) {
			if (!absn.getParentIds().contains(abstractTreeNode.getId())) {
				absn.removeAllPlaces();
			}
		}
	}

	private Set<AbstractTreeNode> getSubtreeNodes(AbstractTreeNode n) {
		Set<AbstractTreeNode> ret = new HashSet<AbstractTreeNode>();
		List<Integer> childern = n.getChildrenIds();
		if (childern != null) {
			for (Integer ch : childern) {
				if (getNodebyId(ch) != null) {
					ret.addAll(getSubtreeNodes(getNodebyId(ch)));
				}
			}
		}
		ret.add(n);
		return ret;
	}

	/**
	 * Have to fix this method
	 * Path till root through each parent has to be cleared
	 * 
	 * @param cn
	 */
	protected void clearTillRoot(AbstractTreeNode cn) {
		// cn has been deleted from tree
		// remove/clear only the ancestors
		// start from parent of cn

		if (cn.getParentIds() == null || cn.getParentIds().isEmpty()) {
			return;
		}
		Queue<Integer> pids = new LinkedList<Integer>();
		Set<Integer> donepids = new HashSet<Integer>();
		pids.addAll(cn.getParentIds());
		while (!pids.isEmpty()) {
			int pid = pids.poll();
			System.out.println("pid " + pid );
			donepids.add(pid);
			AbstractTreeNode pn = getNodebyId(pid);
			if (pn == null) {
				System.out.println("pid " + pid +" caught");
			}
			pn.removeAllPlaces();
			if (pn instanceof CNode) {
				List<Integer> allch = new ArrayList<>(pn.getChildrenIds());
				pn.removeOtherChildrenButMe(cn);
				List<Integer> me = new ArrayList<>(pn.getChildrenIds());
				allch.removeAll(me);
				for (Integer i : allch) {
					removeSubtreeOf(getNodebyId(i));
				}
			}
			if (pn.getParentIds() != null) {
				for (int i : pn.getParentIds()) {
					if (!pids.contains(i) && !donepids.contains(i)) {
						pids.add(i);
					}
				}
			}
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

	protected Set<String> getAllPlaces() {
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
