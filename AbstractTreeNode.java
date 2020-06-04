package treemodel;

import java.util.ArrayList;
import java.util.List;

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

	public String getChildrenIds() {
		if (childrenids != null) {
			StringBuilder sb = new StringBuilder();
			for (int i : childrenids) {
				sb.append(i + " ");
			}
			return sb.toString();
		} else {
			return "none";
		}
	}

	public int howManyChildren() {
		return childrenids.size();
	}
	
	public abstract String getLabel();

}
