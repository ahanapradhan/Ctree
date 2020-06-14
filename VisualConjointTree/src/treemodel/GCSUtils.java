package treemodel;

import java.util.Set;

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
	
	public static Set<String> getAllPlaces(Ctree tree){
		return tree.getAllPlaces();
	}
	
	public static boolean isBreakOffSet(Ctree tree, String[] ps) {
		return tree.isBreakOffSet(ps);
	}
	
	public static boolean doesHaveMPE(Ctree small, Ctree large) {
		return large.doesHaveMPE(small);
	}
}
