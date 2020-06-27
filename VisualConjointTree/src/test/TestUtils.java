package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import parser.CtreeBuilderByECWS;
import treemodel.CNode;

public class TestUtils {

	public final static String ECLIPSE_WKSPC_DIR = "/Users/ahanapradhan/eclipse-workspace/";
	public final static String PROJECT_PARENT_DIR = "/Users/ahanapradhan/eclipse-workspace/VisualConjointTree/";
	public final static String INPUT_NETS_DIR = "/Users/ahanapradhan/eclipse-workspace/VisualConjointTree/src/xml2ecws/sample-nets/";
	public final static String[] PETRINET_XML_FILES = { "andnet1.xml", "andnet2.xml", "andnet3.xml", "andnet4.xml",
			"andxor1.xml", "andxor2.xml", "andnet5.xml", "seq1.xml"};
	
	public final static String[] PETRINETS_FOR_DIRECT_CTREE = {"seq1.xml", "xorctree1.xml", "xorctree2.xml", "loopctree1.xml"};

	public final static String dotFile = "smallgraph.dot";
	public final static String outputFile = "ctree.png";
	public final static String gcsFile = "gcsimg";
	public final static String PNG = ".png";
	public final static String gcsDotFile = "gcs.dot";
	final static String mergedTreeFile = "both.dot";
	final static String mergedTreeImg = "both.png";
	
	public final static String netdotfile = "digraph.dot";
	public final static String netimgfile = "petrinet.png";
	public final static String ctreedotfile = "smallgraph.dot";
	public final static String ctreeimgfile = "ctree.png";
	public final static String mergeddotfile = "merged.dot";
	public final static String mergedimgfile = "merged.png";

	public static void deleteFile(String s) {
		File f = new File(s);
		f.delete();

		if (f.exists()) {
			fail();
		}
	}
	
	public static void deleteFiles() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			fail();
		}
		TestUtils.deleteFile(netdotfile);
		TestUtils.deleteFile(netimgfile);
		TestUtils.deleteFile(ctreedotfile);
		TestUtils.deleteFile(ctreeimgfile);
		TestUtils.deleteFile(mergeddotfile);
		TestUtils.deleteFile(mergedimgfile);
	}

	public static String createTwoImageDot(String img1, String img2) {
		return "graph {\n" 
	+ "    node [shape=none,label=\" \"]\n" 
				+ "    d1 [image=\"" + img1 + "\"]\n"
				+ "    d2 [image=\"" + img2 + "\"]\n" 
				+ "    edge [style=invis]\n" 
				+ "    d1--d2\n" + "}\n";
	}

	public static void createSampleDotFile(File f, String text) {
		try {
			PrintWriter p = new PrintWriter(f);
			p.write(text);
			p.close();
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	public static String readFile(File f) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				sb.append(s + "\n");
			}
			sc.close();
		} catch (FileNotFoundException e) {
			fail();
		}
		return sb.toString();
	}

	@Test
	public void test_dotTest1() {

		String intext = "digraph G {\n" 
		+ "    main -> parse -> execute;\n" 
				+ "    main -> init;\n"
				+ "    main -> cleanup;\n" 
				+ "    execute -> make_string;\n"
				+ "    execute [shape=box,style=filled,color=\".7 .3 1.0\"];\n" 
				+ "    execute -> printf;\n"
				+ "    init -> make_string;\n" 
				+ "    main -> printf;\n" 
				+ "    execute -> compare;\n" 
				+ "}\n";

		File f = new File(dotFile);
		createSampleDotFile(f, intext);
		String printedText = readFile(f);

		assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		runDot(dotFile, pb, outputFile);
		showDotGraphMac(pb, outputFile);

		// deleteFile(outputFile);
	}
	
	@Test
	public void test_dotTest2emptyNode() {

		String intext = "digraph G {\n" 
		+ "    main -> parse -> execute;\n" 
				+ "    parse [label=\"hello\"]\n"
				+ "    main -> init;\n"
				+ "    main -> cleanup;\n" 
				+ "    execute -> make_string;\n"
				+ "    execute [shape=box,style=filled,color=\".7 .3 1.0\"];\n" 
				+ "    execute -> printf;\n"
				+ "    init -> make_string;\n" 
				+ "    main -> printf;\n" 
				+ "    execute -> compare;\n" 
				+ "}\n";

		File f = new File(dotFile);
		createSampleDotFile(f, intext);
		//String printedText = readFile(f);

		//assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		runDot(dotFile, pb, outputFile);
		showDotGraphMac(pb, outputFile);

		// deleteFile(outputFile);
	}

	public static void runDot(String dotFile, ProcessBuilder pb, String outputFile) {
		File f;
		pb.command("/usr/local/bin/dot", "-Tpng", dotFile, "-o", outputFile);
		try {
			Process p = pb.start();

			int exVal = p.waitFor();
			if (exVal == 0) {
				f = new File(outputFile);
				if (!f.exists()) {
					fail();
				}
			} else {
				fail();
			}
		} catch (Exception e) {
			fail();
		}
	}

	public static void showDotGraphMac(ProcessBuilder pb, String outputFile) {
		pb.command("open", outputFile);
		try {
			Process p = pb.start();

			int exVal = p.waitFor();
			if (exVal != 0) {
				fail();
			}
		} catch (Exception e) {
			fail();
		}
	}

	public static String getPackets(String ecws) {
		String[] items = ecws.split(" ");
		List<Object> list = CtreeBuilderByECWS.buildPackets(items);
		StringBuilder sb = new StringBuilder();
		for (Object l : list) {
			if (l instanceof CNode) {
				CNode node = (CNode) l;
				sb.append(node.getLabel());
			} else if (l instanceof String) {
				sb.append(" " + l + " ");
			} else {
				sb.append("-");
			}
		}
		return sb.toString();
	}

}
