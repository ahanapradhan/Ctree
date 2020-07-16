package treemodel;

import java.util.HashSet;
import java.util.Set;

public interface GCSUtils {
	
	public static Set<String> generateMarkings(Ctree tree){
		Set<Set<String>> ms = generateMarkingSet(tree);
		Set<String> markings = new HashSet<String>();
		for (Set<String> m : ms) {
			StringBuilder sb = new StringBuilder();
			String prefix = "";
			for(String s : m) {
				sb.append(prefix);
				sb.append(s);
				prefix = ",";
			}
			markings.add(sb.toString());
		}
		return markings;
	}

	private static Set<Set<String>> generateMarkingSet(Ctree tree) { // testing pending
		Set<Set<String>> mss = null;
		Set<Set<String>> finalSet = new HashSet<Set<String>>();
		Set<String> ps = tree.getAllPlaces();
		if (ps == null || ps.isEmpty()) {
			return null;
		}
		for (String p : ps) {
			mss = generateMarkingSet(getGCS(tree, p));
			if (mss != null) {
				for (Set<String> s : mss) {
					s.add(p);
				}
			} else {
				mss = new HashSet<Set<String>>();
				Set<String> ms = new HashSet<String>();
				ms.add(p);
				mss.add(ms);
			}
			finalSet.addAll(mss);
		}
		return finalSet;
	}

	public static Ctree getGCS(Ctree tree, String place) {
		Ctree gcs = new Ctree(tree);
		for (AbstractTreeNode n : gcs.getNodes()) {
			if (n instanceof CNode) {
				CNode cn = (CNode) n;
				if (cn.doesItHaveIt(place)) {
					gcs.removeNonConcurrentPlaces(cn);
					gcs.cleanGCS();
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
