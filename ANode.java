package treemodel;

public class ANode extends AbstractTreeNode {
    static String labelPrefix = "AND";
    String label;
    
    public String getLabel() {
    	return labelPrefix + String.valueOf(getId());
    }
}
