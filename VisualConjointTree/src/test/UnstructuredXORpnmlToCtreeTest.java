package test;

import java.io.File;

import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import treemodel.Ctree;
import treemodel.CtreeBuilder;
import xml2ecws.PNMLParser;

public class UnstructuredXORpnmlToCtreeTest {
	
	final static String[] PETRI_NET_XML = {"unbalancedXOR1.xml"/*, "unbalancedXOR2.xml", "andnetdouble.xml"*/};
	
	@Test
	public void test_unbalancedXORtest() {
		
		for (String file : PETRI_NET_XML) {
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + file);
			String nettext = m.printNetForDot();
			File f = new File(TestUtils.netdotfile);
			TestUtils.createSampleDotFile(f, nettext);

			ProcessBuilder pb = new ProcessBuilder();
			TestUtils.runDot(TestUtils.netdotfile, pb, TestUtils.netimgfile);

			String ecws = m.getECWS();
			System.out.println(ecws);

			Ctree tree = CtreeBuilder.buildCtree(ecws, m.getName());

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
