package xml2ecws;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import petrinetmodel.Arc;
import petrinetmodel.Net;
import petrinetmodel.Place;
import petrinetmodel.Transition;

public class pnmlParser {
	
	
	/*
	 * PNML file is searched for the following items:
	 * <net id="Net-One" type="P/T net">
	 * <arc id="T2 to P2" source="T2" target="P2">
	 * <place id="P1">
	 * <transition id="T1">
	 * 
	 * The above information is enough to construct the Petri Net Structure.
	 * Nets are assumed to be 1-safe Nets.
	 */
	public static Net readFile(String filename) {
		File f = new File(filename);
		
		Pattern netLabel = Pattern.compile("<net id=\"([^\"]*)\"");		
		Pattern place = Pattern.compile("<place id=\"([^\"]*)\">");
		Pattern transition = Pattern.compile("<transition id=\"([^\"]*)\">");
		Pattern arc = Pattern.compile("<arc id=\"([^\"]*)\" source=\"([^\"]*)\" target=\"([^\"]*)\"");
		
		int placeCount = 0, transitionCount = 0;
		
		Map<String, Integer> placeMap = new HashMap<String, Integer>();
		Map<String, Integer> transitionMap = new HashMap<String, Integer>();
		Map<String, List<String> > arcMap = new HashMap<>();
		
		try {
			Scanner sc = new Scanner(f);		
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				Matcher mNetLabel = netLabel.matcher(line);
				while (mNetLabel.find()) {
				  System.out.println(mNetLabel.group(1)); // may be useful later
				}
				
				Matcher mPlace = place.matcher(line);
				if (mPlace.find()) {
				    placeMap.put(mPlace.group(1), placeCount++);
				}
				
				Matcher mTransition = transition.matcher(line);
				if (mTransition.find()) {
					transitionMap.put(mTransition.group(1), transitionCount++);
				}
				
				Matcher mArc = arc.matcher(line);
				while (mArc.find()) {
				  List<String> info = new ArrayList<String>();
				  info.add(mArc.group(2));
				  info.add(mArc.group(3));
				  arcMap.put(mArc.group(1), info);
				}
			}
			sc.close();
			
			Net pn = createPetriNetModel(placeMap, transitionMap, arcMap);
			return pn;
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return null;
		}		
	}
	
	private static int[][] createNetMatrix(Map<String, Integer> places, 
			Map<String, Integer> transitions, 
			Map<String, List<String>> arcs) {
		
		/*
		 * |transitions| x |places| matrix
		 * p -> t arc : 1
		 * t -> p arc : -1
		 */
		
		int[][] netm = new int[transitions.size()][places.size()];
		
		for (Map.Entry<String, List<String> > e : arcs.entrySet()) {
			String src = e.getValue().get(0);
			String trg = e.getValue().get(1);
			boolean p2t = false;
			int placeIdx = -1, transIdx = -1; // initialize index variable
			
			if (places.containsKey(src) && transitions.containsKey(trg)) {
				p2t = true;
				placeIdx = places.get(src);
				transIdx = transitions.get(trg);
			} 
			else if (places.containsKey(trg) && transitions.containsKey(src)) {
				placeIdx = places.get(trg);
				transIdx = transitions.get(src);
			}
			netm[transIdx][placeIdx] = p2t ? 1 : -1;
		}
	return netm;	
	}
	
	public static Net createPetriNetModel(Map<String, Integer> places, 
			Map<String, Integer> transitions, 
			Map<String, List<String>> arcs) {
		
		Net pn = new Net();
		
		Map<String, Place> ps = new HashMap<String,Place>();
		Map<String,Transition> ts = new HashMap<String, Transition>();
		
		for (Map.Entry<String, Integer> e : places.entrySet()) {
			Place p = new Place(e.getKey());
			ps.put(e.getKey(),p);
		}
		
		for (Map.Entry<String, Integer> e : transitions.entrySet()) {
			Transition t = new Transition(e.getKey());
			ts.put(e.getKey(),t);
		}
		
		pn.setPlaces(ps);
		pn.setTransitions(ts);
		
		List<Arc> pnarcs = new ArrayList<Arc>();
		
		for (Map.Entry<String, List<String> > e : arcs.entrySet()) {
			List<String> srcTrg = e.getValue();
			String src = srcTrg.get(0);
			String trg = srcTrg.get(1);
			Place p;
			Transition t;
			Arc a = null;
			if (ps.containsKey(src) && ts.containsKey(trg)) {
				p = ps.get(src);
				t = ts.get(trg);
				a = new Arc(p,t);		
			} 
			else if (ps.containsKey(trg) && ts.containsKey(src)) {
				p = ps.get(trg);
				t = ts.get(src);
				a = new Arc(t,p);
			}
			if (a !=  null) { // may be null check not needed if everything is proper
				pnarcs.add(a);
			}			
		}
		pn.addArcs(pnarcs);
		return pn;
		
	}
	
}
