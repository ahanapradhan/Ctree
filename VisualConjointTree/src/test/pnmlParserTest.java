package test;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import xml2ecws.pnmlParser;

public class pnmlParserTest {
	static String dotFile = "digraph.dot";
	static String outputFile = "petrinet.png";
    static String workDirectory = "/Users/ahanapradhan/eclipse-workspace/VisualConjointTree/src/xml2ecws/";
    
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		TimeUnit.SECONDS.sleep(3);
		TestUtil.deleteFile(dotFile);
	    TestUtil.deleteFile(outputFile);
	}
	
	@Test
	public void test_andnet1_readNNet() {
		Net m = pnmlParser.readFile(workDirectory+"sample-nets/andnet1.xml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, nettext);
		String printedText = TestUtil.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
		
	}
	
	@Test
	public void test_andnet2_readNNet() {
		Net m = pnmlParser.readFile(workDirectory+"sample-nets/net1.pnml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, nettext);
		String printedText = TestUtil.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
		
	}
	
	@Test
	public void test_andnet3_readNNet() {
		Net m = pnmlParser.readFile(workDirectory+"sample-nets/net2.xml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, nettext);
		String printedText = TestUtil.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
		
	}
}
