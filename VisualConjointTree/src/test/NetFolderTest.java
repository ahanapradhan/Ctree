package test;

import org.junit.jupiter.api.Test;

import parser.FoldedNetToCtreeBuilder;
import parser.NetFolder;
import parser.PNMLParser;
import petrinetmodel.Net;
import treemodel.Ctree;

public class NetFolderTest {

	@Test
	void test_netfolder_allfold() {
		for (int i = 0; i < TestUtils.MULTI_INOUT_NETS.length; i++) {
			System.out.println(TestUtils.MULTI_INOUT_NETS[i]);
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.MULTI_INOUT_NETS[i]);
			Net m1 = new Net(m);
			TestUtils.drawNetAndShow(m1);
			NetFolder.fold(m1);
			TestUtils.drawNetAndShow(m1);
			System.out.println("...done");
		}
	}
	
	@Test
	void test_netfolder_allfold1() {
		for (int i = 0; i < TestUtils.PETRINETS_FOR_DIRECT_CTREE.length; i++) {
			System.out.println(TestUtils.PETRINETS_FOR_DIRECT_CTREE[i]);
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINETS_FOR_DIRECT_CTREE[i]);
			Net m1 = new Net(m);
			TestUtils.drawNetAndShow(m1);
			NetFolder.fold(m1);
			TestUtils.drawNetAndShow(m1);
			Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
			TestUtils.drawCtreeAndShow(tree);
			System.out.println("...done");
		}
	}
	
	@Test
	void test_netfolder_allfold2() {
		for (int i = 0; i < TestUtils.PETRINET_XML_FILES.length; i++) {
			System.out.println(TestUtils.PETRINET_XML_FILES[i]);
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.PETRINET_XML_FILES[i]);
			Net m1 = new Net(m);
			TestUtils.drawNetAndShow(m1);
			NetFolder.fold(m1);
			TestUtils.drawNetAndShow(m1);
			Ctree tree = FoldedNetToCtreeBuilder.buildCtree(m1);
			TestUtils.drawCtreeAndShow(tree);
			System.out.println("...done");
		}
	}
	
	@Test
	void test_netfolder_allfold3() {
		for (int i = 0; i < TestUtils.COMPLEX_NETS.length; i++) {
			System.out.println(TestUtils.COMPLEX_NETS[i]);
			Net m = PNMLParser.readPIPExmlFile(TestUtils.INPUT_NETS_DIR + TestUtils.COMPLEX_NETS[i]);
			Net m1 = new Net(m);
			TestUtils.drawNetAndShow(m1);
			NetFolder.fold(m1);
			TestUtils.drawNetAndShow(m1);
			System.out.println("...done");
		}
	}
}
