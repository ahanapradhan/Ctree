package petrinetmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.TestUtils;
import util.IUtils;
import parser.NetFolder;
import parser.PNMLParser;

class NetTest {
	
	final static String mfile = "m";
	final static String copymfile = "copym";
	final static String mergefile = "merge";
	final static String DOT = ".dot";
	final static String PNG = ".PNG";
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void test_netfolder_test3() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[2]);
		Net m1 = NetFolder.foldWholeNet(m);
		Set<Place> ps = m1.getPlaces();
		String flabel = "";
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0,p1,p2,p3,p4,p5,p6".equals(flabel));
			}
		}
		
		ps = m.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0".equals(flabel));
			}
		}		
	}
	
	@Test
	void test_netfolder_test2() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[1]);
		Net m1 = NetFolder.foldWholeNet(m);
		Set<Place> ps = m1.getPlaces();
		String flabel = "";
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0,p1,p2,p3,p4,p5".equals(flabel));
			}
		}
		
		ps = m.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0".equals(flabel));
			}
		}		
	}
	
	@Test
	void test_netfolder_test1() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[7]);
		Net m1 = NetFolder.foldWholeNet(m);
		Set<Place> ps = m1.getPlaces();
		String flabel = "";
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0,p1,p2,p3,p4".equals(flabel));
			}
		}
		
		ps = m.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				flabel = p.getLabel();
				assertTrue("p0".equals(flabel));
			}
		}
	}
	
	@Test
	void test_netcopy_test1() {
		for (int i = 0; i < TestUtils.PETRINET_XML_FILES.length; i++) {
		    Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[i]);
		    Net copym = new Net(m);
		    
		    String mtext = m.printNetForDot();
			File mf = new File(mfile+DOT);
			TestUtils.createSampleDotFile(mf, mtext);
			ProcessBuilder pb = new ProcessBuilder();
			TestUtils.runDot(mfile+DOT, pb, mfile+PNG);
			
			String copymtext = copym.printNetForDot();
			File copymf = new File(copymfile+DOT);
			TestUtils.createSampleDotFile(copymf, copymtext);
			TestUtils.runDot(copymfile+DOT, pb, copymfile+PNG);
			
			String twotext = TestUtils.createTwoImageDot(mfile+PNG, copymfile+PNG);
			mf = new File(mergefile+DOT);
			TestUtils.createSampleDotFile(mf, twotext);
			TestUtils.runDot(mergefile+DOT, pb, mergefile+PNG);
			TestUtils.showDotGraphMac(pb, mergefile+PNG);
			
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				fail();
			}
			
			TestUtils.deleteFile(mfile+DOT);
			TestUtils.deleteFile(copymfile+DOT);
			TestUtils.deleteFile(mergefile+DOT);
			TestUtils.deleteFile(mfile+PNG);
			TestUtils.deleteFile(copymfile+PNG);
			TestUtils.deleteFile(mergefile+PNG);			
		}
	}

	@Test
	void test() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[0]);
		int i = m.getPlaces().size();
		assertEquals(i, 6);

		for (Place p : m.getPlaces()) {
			Set<Node> pres = p.getPreNodes();
			Set<Node> pros = p.getPostNodes();
			if ("p0".equals(p.getLabel())) {
				assertNull(pres);
				assertEquals(pros.size(), 9);
			}
			if ("p1".equals(p.getLabel())) {
				assertEquals(pres.size(), 2);
				assertEquals(pros.size(), 4);
			}
			if ("p2".equals(p.getLabel())) {
				assertEquals(pres.size(), 2);
				assertEquals(pros.size(), 4);
			}
			if ("p3".equals(p.getLabel())) {
				assertEquals(pres.size(), 4);
				assertEquals(pros.size(), 2);
			}
			if ("p4".equals(p.getLabel())) {
				assertEquals(pres.size(), 4);
				assertEquals(pros.size(), 2);
			}
			if ("p5".equals(p.getLabel())) {
				assertEquals(pres.size(), 9);
				assertNull(pros);
			}
		}

	}

}
