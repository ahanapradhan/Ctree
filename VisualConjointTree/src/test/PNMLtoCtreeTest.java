package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import petrinetmodel.Node;
import petrinetmodel.Place;
import treemodel.Ctree;
import treemodel.CtreeBuilder;
import xml2ecws.pnmlParser;

public class PNMLtoCtreeTest {
	static String netdotfile = "digraph.dot";
	static String netimgfile = "petrinet.png";
	static String ctreedotfile = "smallgraph.dot";
	static String ctreeimgfile = "ctree.png";
	static String mergeddotfile = "merged.dot";
	static String mergedimgfile = "merged.png";
	static String workDirectory = "/Users/ahanapradhan/eclipse-workspace/VisualConjointTree/src/xml2ecws/";

	static String[] xmlFilenames = { "sample-nets/andnet1.xml", "sample-nets/andnet2.xml", "sample-nets/andnet3.xml",
			"sample-nets/andnet4.xml", "sample-nets/andxor1.xml", "sample-nets/andxor2.xml" };

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		// deleteFiles();
	}

	void deleteFiles() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			fail();
		}
		TestUtil.deleteFile(netdotfile);
		TestUtil.deleteFile(netimgfile);
		TestUtil.deleteFile(ctreedotfile);
		TestUtil.deleteFile(ctreeimgfile);
		TestUtil.deleteFile(mergeddotfile);
		TestUtil.deleteFile(mergedimgfile);
	}

	@Test
	public void test_netTest() {
		Net m = pnmlParser.readFile(workDirectory + xmlFilenames[0]);
		int i = m.getPlaces().size();
		assertEquals(i, 6);

		for (Place p : m.getPlaces()) {
			Set<Node> pres = p.getPreNodes();
			Set<Node> pros = p.getPostNodes();
			if("p0".equals(p.getLabel())) {
				assertNull(pres);
				assertEquals(pros.size(),9);
			}
			if("p1".equals(p.getLabel())) {
				assertEquals(pres.size(),2);
				assertEquals(pros.size(),4);
			}
			if("p2".equals(p.getLabel())) {
				assertEquals(pres.size(),2);
				assertEquals(pros.size(),4);
			}
			if("p3".equals(p.getLabel())) {
				assertEquals(pres.size(),4);
				assertEquals(pros.size(),2);
			}
			if("p4".equals(p.getLabel())) {
				assertEquals(pres.size(),4);
				assertEquals(pros.size(),2);
			}
			if("p5".equals(p.getLabel())) {
				assertEquals(pres.size(),9);
				assertNull(pros);
			}
		}

	}

	@Test
	public void test_andnet1_readNNet() {
		for (String file : xmlFilenames) {
			Net m = pnmlParser.readFile(workDirectory + file);
			String nettext = m.printNetForDot();
			File f = new File(netdotfile);
			TestUtil.createSampleDotFile(f, nettext);

			ProcessBuilder pb = new ProcessBuilder();
			TestUtil.runDot(netdotfile, pb, netimgfile);

			String ecws = m.getECWS();
			System.out.println(ecws);

			Ctree tree = CtreeBuilder.buildCtree(ecws);

			String intext = tree.printCtreeForDot();

			f = new File(ctreedotfile);
			TestUtil.createSampleDotFile(f, intext);

			pb = new ProcessBuilder();
			TestUtil.runDot(ctreedotfile, pb, ctreeimgfile);

			f = new File(mergeddotfile);
			String two = TestUtil.createTwoImageDot(netimgfile, ctreeimgfile);
			System.out.println(two);
			TestUtil.createSampleDotFile(f, two);
			pb = new ProcessBuilder();
			TestUtil.runDot(mergeddotfile, pb, mergedimgfile);
			TestUtil.showDotGraphMac(pb, mergedimgfile);

			deleteFiles();
		}

	}

}
