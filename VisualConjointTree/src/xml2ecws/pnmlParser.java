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
import util.IUtils;

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
	public static Net readPIPExmlFile(String filename) {
		File f = new File(filename);
		
		Pattern netLabel = Pattern.compile(IUtils.PIPE_XML_NET_PATTERN_REGEX);		
		Pattern place = Pattern.compile(IUtils.PIPE_XML_PLACE_PATTERN_REGEX);
		Pattern transition = Pattern.compile(IUtils.PIPE_XML_TRANSITION_PATTERN_REGEX);
		Pattern arc = Pattern.compile(IUtils.PIPE_XML_ARC_PATTERN_REGEX);
		
		int placeCount = 0, transitionCount = 0;
		
		Map<String, Integer> placeMap = new HashMap<String, Integer>();
		Map<String, Integer> transitionMap = new HashMap<String, Integer>();
		Map<String, List<String> > arcMap = new HashMap<>();
		
		String netname = "";
		try {
			Scanner sc = new Scanner(f);		
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				
				Matcher mNetLabel = netLabel.matcher(line);
				if (mNetLabel.find()) {
				  netname = mNetLabel.group(1); // may be useful later
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
			pn.setName(netname);
			return pn;
			
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
			return null;
		}		
	}
	
	private static Net createPetriNetModel(Map<String, Integer> places, 
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
			String src = srcTrg.get(IUtils.FIRST_INDEX);
			String trg = srcTrg.get(IUtils.SECOND_INDEX);
			Place p;
			Transition t;
			Arc a = null;
			if (ps.containsKey(src) && ts.containsKey(trg)) {
				p = ps.get(src);
				t = ts.get(trg);
				a = new Arc(p,t);
				p.addOutArc(a);
				t.addInArc(a);
			} 
			else if (ps.containsKey(trg) && ts.containsKey(src)) {
				p = ps.get(trg);
				t = ts.get(src);
				a = new Arc(t,p);
				t.addOutArc(a);
				p.addInArc(a);
			}
			if (a !=  null) { // may be null check not needed if everything is proper
				pnarcs.add(a);
			}			
		}
		pn.addArcs(pnarcs);
		return pn;
		
	}
	
}
