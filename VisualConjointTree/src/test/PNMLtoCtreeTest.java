package test;


import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.FoldedNetToCtreeBuilder;
import parser.PNMLParser;
import petrinetmodel.Net;
import treemodel.Ctree;

public class PNMLtoCtreeTest {

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
		// deleteFiles();
	}

	@Test
	public void test_andnet1_readNNet() {
		for (String file : TestUtils.PETRINET_XML_FILES) {
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + file);
			String nettext = m.printNetForDot();
			File f = new File(TestUtils.netdotfile);
			TestUtils.createSampleDotFile(f, nettext);

			ProcessBuilder pb = new ProcessBuilder();
			TestUtils.runDot(TestUtils.netdotfile, pb, TestUtils.netimgfile);

			Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m);
			tree.setName(m.getName());

			String intext = tree.printCtreeForDot();

			f = new File(TestUtils.ctreedotfile);
			TestUtils.createSampleDotFile(f, intext);

			pb = new ProcessBuilder();
			TestUtils.runDot(TestUtils.ctreedotfile, pb, TestUtils.ctreeimgfile);

			f = new File(TestUtils.mergeddotfile);
			String two = TestUtils.createTwoImageDot(TestUtils.netimgfile, TestUtils.ctreeimgfile);
			System.out.println(two);
			TestUtils.createSampleDotFile(f, two);
			pb = new ProcessBuilder();
			TestUtils.runDot(TestUtils.mergeddotfile, pb, TestUtils.mergedimgfile);
			TestUtils.showDotGraphMac(pb, TestUtils.mergedimgfile);

			TestUtils.deleteFiles();
		}

	}

}
