package treemodel;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import test.TestUtils;
import xml2ecws.pnmlParser;

public class MPEcheckTest {
	final static String bigdot = "big.dot";
	final static String bigimg = "big.png";
	final static String smalldot = "small.dot";
	final static String smallimg = "small.png";
	final static String bigctdot = "bigct.dot";
	final static String bigctimg = "bigct.png";
	final static String smallctdot = "smallct.dot";
	final static String smallctimg = "smallct.png";
	final static String bigfinaldot = "bigfinal.dot";
	final static String bigfinalimg = "bigfinal.png";
	final static String smallfinaldot = "smallfinal.dot";
	final static String smallfinalimg = "smallfinal.png";
	
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
		TestUtils.deleteFile(bigdot);
		TestUtils.deleteFile(bigimg);
		TestUtils.deleteFile(smalldot);
		TestUtils.deleteFile(smallimg);
		TestUtils.deleteFile(bigctdot);
		TestUtils.deleteFile(bigctimg);
		TestUtils.deleteFile(smallctdot);
		TestUtils.deleteFile(smallctimg);
		TestUtils.deleteFile(bigfinaldot);
		TestUtils.deleteFile(bigfinalimg);
		TestUtils.deleteFile(smallfinaldot);
		TestUtils.deleteFile(smallfinalimg);
	}
	
	private void createNetAndCtreeImg(String xmlfile, 
			String dotfile, 
			String imgfile, 
			String ctdot, 
			String ctimg,
			String finaldot,
			String finalimg) {
		ProcessBuilder pb = new ProcessBuilder();
		
		Net m = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + xmlfile);
		String intext = m.printNetForDot();
		File f = new File(dotfile);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(dotfile, pb, imgfile);
		

		String ecws = m.getECWS();
		Ctree tree = CtreeBuilder.buildCtree(ecws);
		intext = tree.printCtreeForDot();
		f = new File(ctdot);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(ctdot, pb, ctimg);
		
		f = new File(finaldot);
		String two = TestUtils.createTwoImageDot(imgfile, ctimg);
		TestUtils.createSampleDotFile(f, two);
		pb = new ProcessBuilder();
		TestUtils.runDot(finaldot, pb, finalimg);
		TestUtils.showDotGraphMac(pb, finalimg);
		
	}
	
	private void check_MPE_bothways(String bignet, String smallnet) {
		Net big = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + bignet);
		Ctree bt = CtreeBuilder.buildCtree(big.getECWS());
		Net small = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + smallnet);
		Ctree st = CtreeBuilder.buildCtree(small.getECWS());
		assertTrue(GCSUtils.doesHaveMPE(st, bt));
		assertFalse(GCSUtils.doesHaveMPE(bt, st));
		assertTrue(GCSUtils.doesHaveMPE(st, st));
		assertTrue(GCSUtils.doesHaveMPE(bt, bt));
	}
	
	private void check_noMPE_bothways(String bignet, String smallnet) {
		Net big = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + bignet);
		Ctree bt = CtreeBuilder.buildCtree(big.getECWS());
		Net small = pnmlParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + smallnet);
		Ctree st = CtreeBuilder.buildCtree(small.getECWS());
		assertFalse(GCSUtils.doesHaveMPE(st, bt));
		assertFalse(GCSUtils.doesHaveMPE(bt, st));
	}
	
	@Test
	public void test_MPEtest() {
		String[] twonets = {"big1.xml", "small1.xml", "big2.xml"};
		//createNetAndCtreeImg(twonets[0], bigdot, bigimg, bigctdot, bigctimg, bigfinaldot, bigfinalimg);
		//createNetAndCtreeImg(twonets[2], smalldot, smallimg, smallctdot, smallctimg, smallfinaldot, smallfinalimg);
		check_noMPE_bothways(twonets[0], twonets[2]);
		check_MPE_bothways(twonets[0], twonets[1]);
	}
	
	@Test
	public void test_MPEtest2() {
		String[] twonets = {"big3.xml", "small2.xml"};
		createNetAndCtreeImg(twonets[0], bigdot, bigimg, bigctdot, bigctimg, bigfinaldot, bigfinalimg);
		createNetAndCtreeImg(twonets[1], smalldot, smallimg, smallctdot, smallctimg, smallfinaldot, smallfinalimg);
		check_noMPE_bothways(twonets[0], twonets[1]);
	}
}
