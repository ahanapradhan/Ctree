package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import treemodel.Ctree;
import treemodel.CtreeBuilder;

class VisCtreeBuilderTest {
	static String dotFile = "smallgraph.dot";
	static String outputFile = "ctree.png";

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
	public void testBuildCtreeV2_doubleNesting_all() {
		Ctree tree = CtreeBuilder.buildCtree("p1 t1 \\( p2 t2 \\( p3 t100 \\( p90 \\) "
				+ "\\( p91 \\) t200 p100 \\) \\( p4 \\) t3 p5 \\) "
				+ "\\( p16 t121 \\{ p21 t22 p33 t31 p44 \\} \\{ t45 p51 t51 \\} "
				+ "t60 p66 \\) t6 p7 t333 \\( p990 \\) \\( p991 \\) \\( p1010 \\) t334 p98");
		String intext = tree.printCtreeForDot();

		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, intext);
		String printedText = TestUtil.readFile(f);

		assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
	}
	
	@Test
	public void test_checkCtree_twoPlusNesting_Test1() {

		Ctree tree = CtreeBuilder
				.buildCtree("p1 t1 p2 t2 \\( p100 t100 p200 \\) \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 5);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		String intext = tree.printCtreeForDot();

		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, intext);
		String printedText = TestUtil.readFile(f);

		assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
	}
	
	@Test
	public void test_checkCtree_twoPlusNesting_Test2() {

		Ctree tree = CtreeBuilder.buildCtree(
				"p1 t1 p2 t2 \\( p201 t101 p202 \\) \\( p100 t100 p200 \\) \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 6);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		//tree.printCtree();
		String intext = tree.printCtreeForDot();

		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, intext);
		String printedText = TestUtil.readFile(f);

		assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
	}

	@Test
	void testBuildCtreeV2_doubleNesting1() {
		Ctree tree = CtreeBuilder.buildCtree("p1 t1 \\( p2 t2 \\( p3 \\) \\( p4 \\) t3 p5 \\) \\( p6 \\) t6 p7");
		String intext = tree.printCtreeForDot();

		File f = new File(dotFile);
		TestUtil.createSampleDotFile(f, intext);
		String printedText = TestUtil.readFile(f);

		assertEquals(intext, printedText);

		ProcessBuilder pb = new ProcessBuilder();
		TestUtil.runDot(dotFile, pb, outputFile);
		TestUtil.showDotGraphMac(pb, outputFile);
	}

	@Test
	void testGetPackets() {
		String s;
		s = TestUtil.getPackets("p1 t1 \\{ p2 t2 p3 t3 p4 \\} \\{ t4 p5 t5 \\} t6 p6");
		assertTrue("p1p2p3p4p5p6".equals(s));

		s = TestUtil.getPackets("p1 t1 p2 t2 \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertTrue("p1p2 \\( p3p4p5 \\)  \\( p6p7 \\) p8p9".equals(s));

		s = TestUtil.getPackets("p1 \\[ t1 p2 t2 \\] \\[ t3 p3 t4 \\] p4");
		assertTrue("p1p2p3p4".equals(s));

		s = TestUtil.getPackets(
				"p1 t1 p2 t2 \\( p11 t8 \\( p3 t3 p4 \\) \\( p5 t4 p6 \\) t5 p9 \\) \\( p7 t6 p8 \\) t7 p106");
		assertTrue("p1p2 \\( p11 \\( p3p4 \\)  \\( p5p6 \\) p9 \\)  \\( p7p8 \\) p106".equals(s));

		s = TestUtil.getPackets("p1 t1 \\{ p2 \\[ t2 p3 t3 \\] \\[ t7 p7 t8 \\] p4 \\} \\{ t4 p5 t5 \\} t6 p6");
		assertTrue("p1p2p3p4p5p6p7".equals(s));

		s = TestUtil.getPackets("p1 t1 p2 t2 p3 t3 p4");
		assertTrue("p1p2p3p4".equals(s));

		s = TestUtil.getPackets(
				"p1 t1 \\{ p2 \\} \\{ t2 p3 t3 \\} t4 \\( p10 t9 p11 \\) \\( p4 \\( p5 t6 p7 \\) \\( p6 t7 p8 \\) p9 \\) t10 p12");
		assertTrue("p1p2p3 \\( p11p10 \\)  \\( p4 \\( p5p7 \\)  \\( p6p8 \\) p9 \\) p12".equals(s));
	}
	
	@Test
	void test_checkall_doubleNestingTest() {

		String s = TestUtil.getPackets(
				"p1 t1 p2 t2 \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 \\( p100 \\) \\( p200 \\) t11 p7 \\) t6 p8 t7 p9");
		assertTrue("p1p2 \\( p3p4p5 \\)  \\( p6 \\( p100 \\)  \\( p200 \\) p7 \\) p8p9".equals(s));
		Ctree tree = CtreeBuilder.buildCtree(
				"p1 t1 p2 t2 \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 \\( p100 \\) \\( p200 \\) t11 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 7);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		//tree.printCtree();
	}

	@Test
	void test_checkCtree_twoPlusNesting_Test() {

		Ctree tree = CtreeBuilder
				.buildCtree("p1 t1 p2 t2 \\( p100 t100 p200 \\) \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 5);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		//tree.printCtree();

		tree = CtreeBuilder.buildCtree(
				"p1 t1 p2 t2 \\( p201 t101 p202 \\) \\( p100 t100 p200 \\) \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 6);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		//tree.printCtree();
	}

	@Test
	void test_checkCtree_basicTest() {
		Ctree tree = CtreeBuilder.buildCtree("p1 t1 \\{ p2 t2 p3 t3 p4 \\} \\{ t4 p5 t5 \\} t6 p6");
		assertEquals(tree.howManyNodes(), 1);
		assertTrue("p1p2p3p4p5p6".equals(tree.getRoot().getLabel()));
		//tree.printCtree();

		tree = CtreeBuilder.buildCtree("p1 t1 p2 t2 \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 4);
		assertTrue("p1p2p8p9".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 1);
		//tree.printCtree();
	}
	
	@Test
	void test_checkCtree_twoPlusNestingSequence_Test() { // two sequential AND -- need to add ANode in Ctree bulding

		Ctree tree = CtreeBuilder.buildCtree(
				"p1 t1 p2 t2 \\( p201 t101 p202 \\) \\( p100 t100 p200 \\) "
				+ "t9 p119 t10 \\( p3 t3 p4 t4 p5 \\) \\( p6 t5 p7 \\) t6 p8 t7 p9");
		assertEquals(tree.howManyNodes(), 7);
		assertTrue("p1p2p8p9p119".equals(tree.getRoot().getLabel()));
		assertEquals(tree.getRoot().howManyChildren(), 2);
		//tree.printCtree();
	}
}
