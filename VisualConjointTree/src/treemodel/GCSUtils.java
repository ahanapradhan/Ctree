package treemodel;

public class GCSUtils {

	public static Ctree getGCS(Ctree tree, String place) {
		Ctree gcs = new Ctree(tree);
		for (AbstractTreeNode n : gcs.getNodes()) {
			if (n instanceof CNode) {
				CNode cn = (CNode) n;
				if (cn.doesItHaveIt(place)) {
					gcs.removeSubtreeOf(cn);
					gcs.clearTillRoot(cn);
					break;
				}
			}
		}
		return gcs;
	}
	
	public static Ctree deletePlacesFrom(Ctree tree, String[] places) {
		tree.deletePlaces(places);
		return tree;
	}
}
