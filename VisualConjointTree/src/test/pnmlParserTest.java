package test;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import xml2ecws.PNMLParser;

public class PNMLParserTest {
	static String dotFile = "digraph.dot";
	static String outputFile = "petrinet.png";
    
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		TimeUnit.SECONDS.sleep(3);
		TestUtils.deleteFile(dotFile);
	    TestUtils.deleteFile(outputFile);
	}
	
	@Test
	public void test_andnet1_readNNet() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR+"andnet1.xml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtils.createSampleDotFile(f, nettext);
		String printedText = TestUtils.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtils.runDot(dotFile, pb, outputFile);
		TestUtils.showDotGraphMac(pb, outputFile);
		
	}
	
	@Test
	public void test_andnet2_readNNet() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR+"net1.pnml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtils.createSampleDotFile(f, nettext);
		String printedText = TestUtils.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtils.runDot(dotFile, pb, outputFile);
		TestUtils.showDotGraphMac(pb, outputFile);
		
	}
	
	@Test
	public void test_andnet3_readNNet() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR+"net2.xml");
		String nettext = m.printNetForDot();
		File f = new File(dotFile);
		TestUtils.createSampleDotFile(f, nettext);
		String printedText = TestUtils.readFile(f);

		assertEquals(nettext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtils.runDot(dotFile, pb, outputFile);
		TestUtils.showDotGraphMac(pb, outputFile);
		
	}
}
