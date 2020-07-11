package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    	resultSet.put("seq1.xml", 6);
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
    }
    
    void test_marking_enumeration() {
    	prepareResultSet();
    	for(Map.Entry<String, Integer> entry : resultSet.entrySet()) {
    		String netname = entry.getKey();
    		int markingCount = entry.getValue();
    		
    		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + netname);
    		Net m1 = new Net(m);
    		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
    		Set<String> markings = GCSUtils.generateMarkings(tree);
    		assertEquals(markings.size(), markingCount);
    	}
    }

	@Test
	void test_marking_generation1() {

		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + "unbalancedXOR1.xml");
		Net m1 = new Net(m);
		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
		TestUtils.drawCtreeAndShow(tree);
		Set<String> markings = GCSUtils.generateMarkings(tree);	
		System.out.println("total "+markings.size()+ " markings");
	}
	
	@Test
	void test_marking_generation2() {

		Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + "unbalancedXOR2.xml");
		Net m1 = new Net(m);
		Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
		TestUtils.drawCtreeAndShow(tree);
		Set<String> markings = GCSUtils.generateMarkings(tree);	
		System.out.println("total "+markings.size()+ " markings");
	}
}
