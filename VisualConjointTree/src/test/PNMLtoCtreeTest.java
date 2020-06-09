package test;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
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
	public void test_andnet1_readNNet() {
		for (String file : TestUtil.PETRINET_XML_FILES) {
			Net m = pnmlParser.readPIPExmlFile(TestUtil.INPUT_NETS_DIR + file);
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
