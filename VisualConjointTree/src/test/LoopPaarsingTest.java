package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import petrinetmodel.Place;
import xml2ecws.pnmlParser;

class LoopPaarsingTest {
	final static String dotFile = "digraph.dot";
	final static String outputFile = "petrinet.png";
	final static String inputnetfile = "loopnet1.xml";

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void test_loopnet1_readNNet() {
		Net m = pnmlParser.readPIPExmlFile(TestUtil.INPUT_NETS_DIR + inputnetfile);
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, nettext);
		String printedText = TestUtil.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);

		Set<Place> ps = m.getPlaces();
		for (Place p : ps) {
			if ("p1".equals(p.getLabel()) || "p2".equals(p.getLabel())) {
				assertTrue(p.doesItHaveLoopArc());
				// p1 is loop fork.
				// p2 is loop join.
			} else {
				assertFalse(p.doesItHaveLoopArc());
			}

		}

	}

}
