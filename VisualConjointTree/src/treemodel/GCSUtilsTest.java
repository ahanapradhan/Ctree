package treemodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import test.TestUtils;
import xml2ecws.pnmlParser;

class GCSUtilsTest {
	static String netdotfile = "digraph.dot";
	static String netimgfile = "petrinet.png";

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		deleteFiles();
	}
	
	void deleteFiles() {
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			fail();
		}
		TestUtils.deleteFile(netdotfile);
		TestUtils.deleteFile(netimgfile);
	}
	
	@Ignore
	@Test
	public void test_listdiff() {
		List<Integer> a = new ArrayList<Integer>();
		a.add(1);
		a.add(2);
		a.add(3);
		a.add(4);
		List<Integer> b = new ArrayList<Integer>();
		b.add(1);
		b.add(4);
		a.removeAll(b);
		assertEquals(a.size(), 2);
		assertTrue(a.contains(2));
		assertTrue(a.contains(3));
	}

	@Ignore
	@Test
	public void testBuildCtreeV2_doubleNesting_all() {
		
		Net m = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[6]);
		String nettext = m.printNetForDot();
		File f = new File(netdotfile);
		TestUtils.createSampleDotFile(f, nettext);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtils.runDot(netdotfile, pb, netimgfile);
		TestUtils.showDotGraphMac(pb, netimgfile);

		String ecws = m.getECWS();
		System.out.println(ecws);

		Ctree tree = CtreeBuilder.buildCtree(ecws);
		
		tree.setName("Net1");
		String intext = tree.printCtreeForDot();


		f = new File(TestUtils.dotFile);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(TestUtils.dotFile, pb, TestUtils.outputFile);
        TestUtils.showDotGraphMac(pb, TestUtils.outputFile);
		
		createGCSfor(new String[]{"p1", "p2", "p14", "p19", "p22"},  tree);
	}
	
	private void createGCSfor(String[] places, Ctree t) {
		ProcessBuilder pb = new ProcessBuilder();
		List<String> imgfiles = new ArrayList<String>();

		for(String p : places) {
			Ctree gcstree = GCSUtils.getGCS(t,p);
			gcstree.setName("GCS of "+ p +" in "+t.getName());
			String gcstext = gcstree.printCtreeForDot();
			File f = new File(TestUtils.gcsDotFile);
			TestUtils.createSampleDotFile(f, gcstext);
			
			TestUtils.runDot(TestUtils.gcsDotFile, pb, TestUtils.gcsFile+p+TestUtils.PNG);
			imgfiles.add(TestUtils.gcsFile+p+TestUtils.PNG);
			TestUtils.showDotGraphMac(pb, TestUtils.gcsFile+p+TestUtils.PNG);
		}
		
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			fail();
		}
		for(String p : imgfiles) {
			TestUtils.deleteFile(p);
		}
	}
	
	@Test
	public void test_deletePlacesFromCtree() {
		Net m = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[3]);
		String nettext = m.printNetForDot();
		File f = new File(netdotfile);
		TestUtils.createSampleDotFile(f, nettext);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtils.runDot(netdotfile, pb, netimgfile);
		TestUtils.showDotGraphMac(pb, netimgfile);

		String ecws = m.getECWS();
		System.out.println(ecws);

		Ctree tree = CtreeBuilder.buildCtree(ecws);
		
		tree.setName("Net1");
		String intext = tree.printCtreeForDot();


		f = new File(TestUtils.dotFile);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(TestUtils.dotFile, pb, TestUtils.outputFile);
        TestUtils.showDotGraphMac(pb, TestUtils.outputFile);
        
        GCSUtils.deletePlacesFrom(tree, new String[]{"p14", "p16", "p12", "p6", "p9", "p2", "p3", "p0", "p11", "p5"});
        intext = tree.printCtreeForDot();


		f = new File("deleted.dot");
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot("deleted.dot", pb, "deleted.png");
        TestUtils.showDotGraphMac(pb, "deleted.png");
        
        try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			fail();
		}
		TestUtils.deleteFile("deleted.dot");
		TestUtils.deleteFile("deleted.png");
	}

}
