package treemodel;

import java.util.HashSet;
import java.util.Set;

public interface GCSUtils {

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
	
	public static Ctree deletePlacesFrom(Ctree tree, Set<String> places) {
		Ctree copy = new Ctree(tree);
		copy.deletePlaces(places);
		return copy;
	}
	
	public static Set<String> getAllPlaces(Ctree tree){
		return tree.getAllPlaces();
	}
	
	public static Set<String> getRootPlaces(Ctree tree){
		return tree.getRoot().getPlaces();
	}
	
	public static Set<String> getNonRootPlaces(Ctree tree){
		Set<String> all = new HashSet<String>(tree.getAllPlaces());
		Set<String> roots = new HashSet<String>(getRootPlaces(tree));
		all.removeAll(roots);
		return all;
	}
	
	public static boolean isBreakOffSet(Ctree tree, Set<String> ps) {
		Ctree copy = new Ctree(tree);
		return copy.isBreakOffSet(ps);
	}
	
	public static boolean doesHaveMPE(Ctree small, Ctree large) {
		return large.doesHaveMPE(small);
	}
}
