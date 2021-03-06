package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import parser.FoldedNetToCtreeBuilder;
import parser.PNMLParser;
import petrinetmodel.Net;
import treemodel.Ctree;
import treemodel.GCSUtils;

public class MarkingGenerationTest {

	static Map<String, Integer> resultSet;

	private void prepareResultSet() {
		resultSet = new HashMap<String, Integer>();
		resultSet.put("andnet5.xml", 53);
		resultSet.put("andnet4.xml", 34);
		resultSet.put("andnet3.xml", 16);
		resultSet.put("andnet2.xml", 14);
		resultSet.put("andnet1.xml", 6);
		resultSet.put("seq1.xml", 5);
		resultSet.put("andxor1.xml", 10);
		resultSet.put("andxor2.xml", 10);
		resultSet.put("andnet45.xml", 51);
		resultSet.put("andnet55.xml", 37);
		resultSet.put("big1.xml", 7);
		resultSet.put("big2.xml", 7);
		resultSet.put("big3.xml", 19);
		resultSet.put("andnetdouble.xml", 4);
		resultSet.put("loopctree1.xml", 7);
		resultSet.put("loopctree2.xml", 9);
		resultSet.put("loopnet1.xml", 5);
		resultSet.put("net2.xml", 16);
		resultSet.put("xorctree1.xml", 6);
		resultSet.put("xorctree2.xml", 7);
		resultSet.put("xorctree3.xml", 5);
		resultSet.put("small1.xml", 5);
		resultSet.put("small2.xml", 8);
		resultSet.put("multiinOutT1.xml", 5);
		resultSet.put("multiinOutT2.xml", 6);
		resultSet.put("uand1.xml", 6);
		resultSet.put("uand3.xml", 8);
		resultSet.put("uand4.xml", 7);
		resultSet.put("uand5.xml", 6);
		resultSet.put("uand2_4.xml", 33);
		//resultSet.put("uand2_3_1.xml", 21);
		resultSet.put("uand2_1.xml", 12);
		resultSet.put("diamondnet1.xml", 7);
		resultSet.put("diamondnet2.xml", 6);
		resultSet.put("diamondnet3.xml", 6);
		resultSet.put("diamondnet4.xml", 5);
		resultSet.put("diamondnet5.xml", 11);
		resultSet.put("diamondnet.xml", 10);
		resultSet.put("houseConstruction10.xml", 66);
	}

	@Test
	void test_marking_enumeration() {
		prepareResultSet();
		for (Map.Entry<String, Integer> entry : resultSet.entrySet()) {
			String netname = entry.getKey();
			int markingCount = entry.getValue();

			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + netname);
			Net m1 = new Net(m);
			//TestUtils.drawNetAndShow(m1);
			Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
			Set<String> markings = GCSUtils.generateMarkings(tree);
			System.out.println(netname);
			/*for(String p : markings) {
				System.out.println(p);
			}*/
			assertEquals(markings.size(), markingCount);

		}
	}

	@Test
	void test_marking_genUand3() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + "uand3.xml");
		Net m1 = new Net(m);
		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
		Set<String> markings = GCSUtils.generateMarkings(tree);
		assertTrue(markings.contains("p0"));
		assertTrue(markings.contains("p6"));
		assertTrue(markings.contains("p1,p2,p3"));
		assertTrue(markings.contains("p9"));
		assertTrue(markings.contains("p7,p8"));
		assertTrue(markings.contains("p3,p4"));
		assertTrue(markings.contains("p1,p2,p5"));
		assertTrue(markings.contains("p4,p5"));
		assertEquals(markings.size(), 8);
	}

	@Test
	void test_marking_genUand4() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + "uand4.xml");
		Net m1 = new Net(m);
		TestUtils.drawNetAndShow(m1);
		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
		Set<String> markings = GCSUtils.generateMarkings(tree);
		assertTrue(markings.contains("p4,p8"));
		assertTrue(markings.contains("p6"));
		assertTrue(markings.contains("p9"));
		assertTrue(markings.contains("p5,p7"));
		assertTrue(markings.contains("p10"));
		assertTrue(markings.contains("p7,p8"));
		assertTrue(markings.contains("p4,p5"));
		assertEquals(markings.size(), 7);
	}

	@Test
	void test_marking_genUand5() {
		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + "uand5.xml");
		Net m1 = new Net(m);
		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
		Set<String> markings = GCSUtils.generateMarkings(tree);
		assertTrue(markings.contains("p3,p4"));
		assertTrue(markings.contains("p6"));
		assertTrue(markings.contains("p0"));
		assertTrue(markings.contains("p1,p2,p3"));
		assertTrue(markings.contains("p1,p2,p5"));
		assertTrue(markings.contains("p4,p5"));
		assertEquals(markings.size(), 6);
	}
}
