package treemodel;

import java.util.ArrayList;
import java.util.List;

import util.IUtils;

public abstract class AbstractTreeNode {
	static int uniq = 0;
	int id;
	List<Integer> childrenids;
	List<Integer> parentids;

	public AbstractTreeNode() {
		id = uniq++;
		childrenids = null;
		parentids = null;
	}

	public AbstractTreeNode(AbstractTreeNode a) {
		// only used for copy construction
		id = a.id;
		if (a.childrenids != null) {
			childrenids = new ArrayList<Integer>(a.childrenids);
		} else {
			childrenids = null;
		}
		
		if (a.parentids != null) {
			parentids = new ArrayList<Integer>(a.parentids);
		} else {
			parentids = null;
		}
	}
	
	public void removeOtherChildrenButMe(AbstractTreeNode n) {
		childrenids.clear();
		childrenids.add(n.getId());
	}

	public int getId() {
		return id;
	}

	public void addChild(int i) { // adds another CNode as child
		if (childrenids == null) {
			childrenids = new ArrayList<Integer>();
		}
		childrenids.add(i);
	}
	
	public void addParent(int i) { 
		if (parentids == null) {
			parentids = new ArrayList<Integer>();
		}
		parentids.add(i);
	}

	public List<Integer> getChildrenIds() {
		return childrenids;
	}
	
	public List<Integer> getParentIds() {
		return parentids;
	}

	public int howManyChildren() {
		if (childrenids == null) {
			return IUtils.ZERO;
		}
		return childrenids.size();
	}
	
	public abstract String getLabel();

	public abstract String getVisLabel();

	protected void removeAllPlaces() {
	}

	public void removeChild(int id) {
		for (int i = 0; i < childrenids.size(); i++) {
			if (childrenids.get(i) == id) {
				childrenids.remove(i);
			}
		}
	}
	
	protected boolean isThereNoPlace() {
		return true;
	}

}
