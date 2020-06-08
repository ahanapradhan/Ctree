package treemodel;

import java.util.ArrayList;
import java.util.List;

public class CtreeBuilder {

	
	public static Ctree buildCtree(String ecws) {

		String[] items = ecws.split(" ");
		Ctree tree = new Ctree();
		List<Object> list = buildPackets(items);

		AbstractTreeNode currentNode = null, parentNode = null, andNode = null;
		Object prevo = null, onext, onnext, o;

		for (int i = 0; i < list.size();) {
			o = list.get(i++);
			if (o instanceof CNode && prevo == null) {
				currentNode = (CNode) o;
				tree.addNode(currentNode);
				tree.setRoot((CNode) currentNode);
			}
			else {
				onext = list.get(i++);
				if (o instanceof String && "\\(".equals(o) && onext instanceof CNode) {
					parentNode = currentNode;
					andNode = new ANode();
					parentNode.addChild(andNode.getId());
					andNode.setParent(parentNode.getId());
					currentNode = (CNode) onext;
					currentNode.setParent(andNode.getId());
					andNode.addChild(currentNode.getId());
					tree.addNode(andNode);
					tree.addNode(currentNode);
				}
				else if (o instanceof String && "\\)".equals(o) && onext instanceof CNode) {
					currentNode = parentNode;
					andNode = tree.getParentNode(currentNode);
					parentNode = tree.getParentNode(andNode);
					((CNode) currentNode).addPlaces(((CNode) onext).getPlaces());
				}
				else if (o instanceof String && "\\)".equals(o) 
						&& onext instanceof String && "\\(".equals(onext)) {
					onnext = list.get(i++);
					if (onnext instanceof CNode) {
						currentNode = (CNode) onnext;
						tree.addNode(currentNode);
						currentNode.setParent(andNode.getId());
						andNode.addChild(currentNode.getId());
					}					
				}
			}
			prevo = list.get(i-1);
		}
		return tree;
	}

	public static List<Object> buildPackets(String[] items) {
		List<Object> list = new ArrayList<Object>();

		CNode node = null;
		for (String s : items) {
			// System.out.println(s);
			if (s.startsWith("p")) {
				if (node == null) {
					node = new CNode();
				}
				node.addPlace(s);
			} else if (s.contains("\\(") || s.contains("\\)")) {
				if (node != null) {
					list.add(node);
					node = null;
				}
				list.add(s);
			}
		}
		if (node != null) {
			list.add(node);
		}

		return list;
	}

}
