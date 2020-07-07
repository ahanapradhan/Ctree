package petrinetmodel;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.NetFolder;
import parser.PNMLParser;
import test.TestUtils;
import util.IUtils;

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
	void test_netfolder_Andnet_nonFold() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[1]);
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		NetFolder.foldOneInOneOutTransitions(m1);
		TestUtils.drawNetAndShow(m1);
		assertEquals(7, m1.getPlaces().size());
		assertEquals(4, m1.getTransitions().size());
	}
	
	@Test
	void test_netfolder_seqfold() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[2]);
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		Set<Place> ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p6")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p3,p5,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(5, m1.getPlaces().size());
				break;
			}
		}
		ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p2")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p2,p3,p5,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(4, m1.getPlaces().size());
				break;
			}
		}
		ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p1")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p1,p2,p3,p4,p5,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(2, m1.getPlaces().size());
				break;
			}
		}
	}
	
	@Test
	void test_netfolder_test_loop() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[3]);
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		NetFolder.foldOneInOneOutTransitions(m1);
		Set<Place> ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				assertTrue("p0,p1,p2,p3,p4,p5,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				break;
			}
		}
	}
	
	@Test
	void test_netfolder_test6() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[2]);
		Net m1 = new Net(m);
		
		NetFolder.foldOneInOneOutTransitions(m1);
		Set<Place> ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				assertTrue("p0,p1,p2,p3,p4,p5,p6".equals(p.getLabel()));
				break;
			}
		}
	}
	
	@Test
	void test_netfolder_test5() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[1]);
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		Set<Place> ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				TestUtils.drawNetAndShow(m1);
				assertTrue("p0,p1,p2,p3,p4,p5".equals(p.getLabel()));
				assertEquals(m1.getPlaces().size(), 1);
				assertEquals(m1.getTransitions().size(), 0);
				assertEquals(m1.getArcs().size(), 0);
				break;
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
	void test_netfolder_stepbystepFolding1() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[5]);
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		Set<Place> ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p6")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p3,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(4, m1.getPlaces().size());
				break;
			}
		}
		ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p4")) {

				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p3,p4,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(3, m1.getPlaces().size());
				break;
			}
		}
		ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p2")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p2,p3,p4,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(2, m1.getPlaces().size());
				break;
			}
		}
		ps = m1.getPlaces();
		for (Place p : ps) {
			if (p.getLabel().equals("p1")) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p1,p2,p3,p4,p6".equals(p.getLabel()));
				TestUtils.drawNetAndShow(m1);
				assertEquals(1, m1.getPlaces().size());
				break;
			}
		}
	}
	
	@Test
	void test_netfolder_test4() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[0]);
		Net m1 = new Net(m);
		Set<Place> ps = m1.getPlaces();
		String flabel = "";
		for (Place p : ps) {
			if (p.howManyInArcs() == IUtils.ZERO) {
				NetFolder.foldPostTransitions(m1, p, IUtils.SINGLE_IN_OUT);
				assertTrue("p0,p1,p2,p3,p4".equals(p.getLabel()));
				assertEquals(m1.getPlaces().size(), 1);
				assertEquals(m1.getTransitions().size(), 0);
				assertEquals(m1.getArcs().size(), 0);
				break;
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

}
