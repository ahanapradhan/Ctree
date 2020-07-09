package parser;

import java.util.HashSet;
import java.util.Set;

import petrinetmodel.Net;
import treemodel.Ctree;
import treemodel.GCSUtils;
import util.IUtils;

public interface PSCRUtils {

	public static Set<String> getPSCR(String oldnetfilename, String newnetfilename) {
		Net oldnet = PNMLParser.readPIPExmlFile(oldnetfilename);
		Net newnet = PNMLParser.readPIPExmlFile(newnetfilename);

		Ctree oldtree = FoldedNetToCtreeBuilder.buildCtree(oldnet);
		oldtree.setName(oldnet.getName());
		Ctree newtree = FoldedNetToCtreeBuilder.buildCtree(newnet);
		newtree.setName(newnet.getName());

		Set<String> CRr = getRemovalSet(oldtree, newtree);
		Set<String> CRlc = getLostConcurrencySet(oldtree, newtree);
		Set<String> CRac = getAcquiredConcurrencySet(oldtree, newtree);
		Set<String> CRwrc = getWeakReformedConcurrencySet(oldtree, newtree);
		Set<String> CRsrc = getStrongReformedConcurrency(oldtree, newtree);

		Set<String> Over = new HashSet<String>(CRwrc);
		Over.removeAll(CRsrc);

		Set<String> Perf = new HashSet<String>(CRr);
		Perf.addAll(CRlc);
		Perf.addAll(CRac);
		Perf.addAll(CRsrc);

		Set<String> SCR = new HashSet<String>(Over);
		SCR.addAll(Perf);

		if (isPerfexist(oldtree, newtree, Over, Perf)) {
			return Perf;
		}
		return SCR;
	}

	private static boolean isPerfexist(Ctree oldtree, Ctree newtree, Set<String> over, Set<String> perf) {
		if (over == null || over.size() == IUtils.ZERO) {
			return true;
		}
		if (GCSUtils.isBreakOffSet(oldtree, perf)) {
			return true;
		}
		if (GCSUtils.isBreakOffSet(newtree, perf)) {
			return false;
		}
		if (!GCSUtils.doesHaveMPE(GCSUtils.deletePlacesFrom(oldtree, perf), GCSUtils.deletePlacesFrom(newtree, perf))) {
			return false;
		}
		return true;
	}

	private static Set<String> getStrongReformedConcurrency(Ctree oldtree, Ctree newtree) {
		Set<String> oldnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(oldtree));
		Set<String> newnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(newtree));
		oldnonrootplaces.retainAll(newnonrootplaces); // common
		Set<String> src = new HashSet<String>();
		for (String p : oldnonrootplaces) {
			Ctree oldgcs = GCSUtils.getGCS(oldtree, p);
			Ctree newgcs = GCSUtils.getGCS(newtree, p);
			Set<String> oldgcsplaces = GCSUtils.getAllPlaces(oldgcs);
			Set<String> newgcsplaces = GCSUtils.getAllPlaces(newgcs);
			Set<String> oldminusnew = new HashSet<String>(oldgcsplaces);
			oldminusnew.removeAll(newgcsplaces);
			Set<String> newminusold = new HashSet<String>(newgcsplaces);
			newminusold.removeAll(oldgcsplaces);
			if (GCSUtils.isBreakOffSet(oldgcs, oldminusnew) || GCSUtils.isBreakOffSet(newgcs, newminusold)) {
				src.add(p);
			}
		}
		return src;
	}

	private static Set<String> getWeakReformedConcurrencySet(Ctree oldtree, Ctree newtree) {
		Set<String> oldnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(oldtree));
		Set<String> newnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(newtree));
		oldnonrootplaces.retainAll(newnonrootplaces); // common
		Set<String> wrc = new HashSet<String>();
		for (String p : oldnonrootplaces) {
			//System.out.println(oldtree.printCtreeForDot());
			Ctree oldgcs = GCSUtils.getGCS(oldtree, p);
			//System.out.println(oldgcs.printCtreeForDot());
			Ctree newgcs = GCSUtils.getGCS(newtree, p);
			//System.out.println(newgcs.printCtreeForDot());
			if (!GCSUtils.doesHaveMPE(oldgcs, newgcs)) {
				wrc.add(p);
			}
		}
		return wrc;
	}

	private static Set<String> getAcquiredConcurrencySet(Ctree oldtree, Ctree newtree) {
		Set<String> oldrootplaces = new HashSet<String>(GCSUtils.getRootPlaces(oldtree));
		Set<String> newnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(newtree));
		newnonrootplaces.retainAll(oldrootplaces);
		return newnonrootplaces;
	}

	private static Set<String> getLostConcurrencySet(Ctree oldtree, Ctree newtree) {
		Set<String> newrootplaces = new HashSet<String>(GCSUtils.getRootPlaces(newtree));
		Set<String> oldnonrootplaces = new HashSet<String>(GCSUtils.getNonRootPlaces(oldtree));
		oldnonrootplaces.retainAll(newrootplaces);
		return oldnonrootplaces;
	}

	private static Set<String> getRemovalSet(Ctree oldtree, Ctree newtree) {
		Set<String> oldplaces = new HashSet<String>(GCSUtils.getAllPlaces(oldtree));
		Set<String> newplaces = new HashSet<String>(GCSUtils.getAllPlaces(newtree));
		oldplaces.removeAll(newplaces);
		return oldplaces;
	}

}
