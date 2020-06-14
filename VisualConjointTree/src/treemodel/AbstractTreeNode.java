package treemodel;

import java.util.ArrayList;
import java.util.List;

import util.IUtils;

public abstract class AbstractTreeNode {
	static int uniq = 0;
	int id;
	int parentid;
	List<Integer> childrenids;

	public AbstractTreeNode() {
		id = uniq++;
		parentid = -1;
		childrenids = null;
	}

	public AbstractTreeNode(AbstractTreeNode a) {
		// only used for copy construction
		id = a.id;
		parentid = a.parentid;
		if (a.childrenids != null) {
			childrenids = new ArrayList<Integer>(a.childrenids);
		} else {
			childrenids = null;
		}
	}
	
	public void removeOtherChildrenButMe(AbstractTreeNode n) {
		childrenids.clear();
		childrenids.add(n.getId());
	}

	public int getId() {
		return id;
	}

	public void setParent(int i) {
		parentid = i;
	}

	public int getParent() {
		return parentid;
	}

	public void addChild(int i) { // adds another CNode as child
		if (childrenids == null) {
			childrenids = new ArrayList<Integer>();
		}
		childrenids.add(i);
	}

	public List<Integer> getChildrenIds() {
		return childrenids;
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
