package treemodel;

public class ANode extends AbstractTreeNode {
    static String labelPrefix = "a";
    String label;
    
    @Override
    public String getLabel() {
    	return labelPrefix + String.valueOf(getId());
    }

	@Override
	public String getVisLabel() {
		return getLabel();
	}
}
