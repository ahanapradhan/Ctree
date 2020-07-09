package parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import petrinetmodel.Arc;
import petrinetmodel.Net;
import petrinetmodel.Node;
import petrinetmodel.Place;
import petrinetmodel.Transition;
import treemodel.ANode;
import treemodel.AbstractTreeNode;
import treemodel.CNode;
import treemodel.Ctree;
import util.IUtils;

public interface FoldedNetToCtreeBuilder {
	
	public static Ctree buildCtree(Net neto) {
		Net net = new Net(neto);
		NetFolder.fold(net);
		Ctree tree = new Ctree();
		Map<Node, Integer> map = new HashMap<Node, Integer>();
		for(Place p : net.getPlaces()) {
			CNode cn = buildCNode(p);
			map.put(p, cn.getId());
			tree.addNode(cn);
			if (p.howManyInArcs() == IUtils.ZERO) {
				tree.setRoot(cn);
			}
		}
		for(Transition t : net.getTransitions()) {
			ANode an = buildANode(t);
			map.put(t, an.getId());
			tree.addNode(an);
		}
		
		Set<AbstractTreeNode> treenodes = tree.getNodes();
		Map<Integer, AbstractTreeNode> tmap = new HashMap<Integer, AbstractTreeNode>();
		for (AbstractTreeNode atn : treenodes) {
			tmap.put(atn.getId(), atn);
		}
		for(Arc a : net.getArcs()) {
			Node in = a.getInNode();
			Node out = a.getOutNode();
			int parentId = map.get(in);
			int childId = map.get(out);
			AbstractTreeNode parent = tmap.get(parentId);
			AbstractTreeNode child = tmap.get(childId);
			parent.addChild(childId);
			child.setParent(parentId);		
		}
		
		return tree;
	}
	
	static CNode buildCNode(Place p) {
		CNode cn = new CNode();
		String[] places = p.getLabel().split(",");
		for (String s : places) {
			cn.addPlace(s);
		}
		return cn;
	}
	
	static ANode buildANode(Transition t) {
		ANode an = new ANode();
		return an;
	}
}
