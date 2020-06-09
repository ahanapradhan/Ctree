package treemodel;

public class ANode extends AbstractTreeNode {
    final static String LABEL_PREFIX = "a";
    String label;
    
    @Override
    public String getLabel() {
    	return LABEL_PREFIX + String.valueOf(getId());
    }

	@Override
	public String getVisLabel() {
		return getLabel();
	}
}
