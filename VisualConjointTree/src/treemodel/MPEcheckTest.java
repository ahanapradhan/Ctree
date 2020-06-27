package treemodel;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import petrinetmodel.Net;
import test.TestUtils;
import parser.CtreeBuilderByECWS;
import parser.PNMLParser;
import parser.PSCR;

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
			Set<String> cr,
			String netname) {
		ProcessBuilder pb = new ProcessBuilder();
		
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + xmlfile);
		m.setName(netname);
		String intext = "";
		if (cr != null) {
		  intext = m.printNetForDot(cr);
		}
		else {
			intext = m.printNetForDot();
		}
			
		File f = new File(dotfile);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(dotfile, pb, imgfile);
		

		String ecws = m.getECWS();
		Ctree tree = CtreeBuilderByECWS.buildCtree(ecws, m.getName());
		intext = tree.printCtreeForDot();
		f = new File(ctdot);
		TestUtils.createSampleDotFile(f, intext);
		TestUtils.runDot(ctdot, pb, ctimg);
		
	}
	
	public static void createAllImageDot(
			String img1, 
			String img2, 
			String img3, 
			String img4) {
		ProcessBuilder pb = new ProcessBuilder();
		String text1 = TestUtils.createTwoImageDot(img1, img2);
		File f1 = new File("dotupdwn.dot");
		TestUtils.createSampleDotFile(f1, text1);
		System.out.println(text1);
		TestUtils.runDot("dotupdwn.dot", pb, "updown.png");
				
		String text2 = "graph {\n"
				+ "    rankdir = LR\n"
				+ "    node [shape=none,label=\" \"]\n" 
							+ "    d1 [image=\"" + img3 + "\"]\n"
							+ "    d2 [image=\"" + img4 + "\"]\n" 
							+ "    edge [style=invis]\n" 
							+ "    d1--d2\n" + "}\n";
		File f2 = new File("dotsidebyside.dot");
		TestUtils.createSampleDotFile(f2, text2);
		TestUtils.runDot("dotsidebyside.dot", pb, "sidebyside.png");
		
		String merged = TestUtils.createTwoImageDot("updown.png", "sidebyside.png");
		f1 = new File(bigfinaldot);
		TestUtils.createSampleDotFile(f1, merged);
		TestUtils.runDot(bigfinaldot, pb, bigfinalimg);
		TestUtils.showDotGraphMac(pb, bigfinalimg);
	}
	
	private void check_MPE_bothways(String bignet, String smallnet) {
		Net big = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + bignet);
		Ctree bt = CtreeBuilderByECWS.buildCtree(big.getECWS(), big.getName());
		Net small = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + smallnet);
		Ctree st = CtreeBuilderByECWS.buildCtree(small.getECWS(), small.getName());
		assertTrue(GCSUtils.doesHaveMPE(st, bt));
		assertFalse(GCSUtils.doesHaveMPE(bt, st));
		assertTrue(GCSUtils.doesHaveMPE(st, st));
		assertTrue(GCSUtils.doesHaveMPE(bt, bt));
	}
	
	private void check_noMPE_bothways(String bignet, String smallnet) {
		Net big = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + bignet);
		Ctree bt = CtreeBuilderByECWS.buildCtree(big.getECWS(), big.getName());
		Net small = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + smallnet);
		Ctree st = CtreeBuilderByECWS.buildCtree(small.getECWS(), small.getName());
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
		//createNetAndCtreeImg(twonets[0], bigdot, bigimg, bigctdot, bigctimg, bigfinaldot, bigfinalimg);
		//createNetAndCtreeImg(twonets[1], smalldot, smallimg, smallctdot, smallctimg, smallfinaldot, smallfinalimg);
		check_noMPE_bothways(twonets[0], twonets[1]);
	}
	
	@Test
	public void test_PSCRtest() {
		String[] twonets = {"andnet55.xml", "andnet5.xml"};
		Set<String> pscr = PSCR.getPSCR(TestUtils.INPUT_NETS_DIR + twonets[0], TestUtils.INPUT_NETS_DIR + twonets[1]);
		createNetAndCtreeImg(twonets[1], bigdot, bigimg, bigctdot, bigctimg, null, "New Net");
		createNetAndCtreeImg(twonets[0], smalldot, smallimg, smallctdot, smallctimg, pscr, "Old Net");
		createAllImageDot(bigimg, smallimg, bigctimg, smallctimg);

	}
}
