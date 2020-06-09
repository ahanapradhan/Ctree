package petrinetmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.TestUtil;
import xml2ecws.pnmlParser;

class NetTest {
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Net m = pnmlParser.readPIPExmlFile(TestUtil.INPUT_NETS_DIR + TestUtil.PETRINET_XML_FILES[0]);
		int i = m.getPlaces().size();
		assertEquals(i, 6);

		for (Place p : m.getPlaces()) {
			Set<Node> pres = p.getPreNodes();
			Set<Node> pros = p.getPostNodes();
			if ("p0".equals(p.getLabel())) {
				assertNull(pres);
				assertEquals(pros.size(), 9);
			}
			if ("p1".equals(p.getLabel())) {
				assertEquals(pres.size(), 2);
				assertEquals(pros.size(), 4);
			}
			if ("p2".equals(p.getLabel())) {
				assertEquals(pres.size(), 2);
				assertEquals(pros.size(), 4);
			}
			if ("p3".equals(p.getLabel())) {
				assertEquals(pres.size(), 4);
				assertEquals(pros.size(), 2);
			}
			if ("p4".equals(p.getLabel())) {
				assertEquals(pres.size(), 4);
				assertEquals(pros.size(), 2);
			}
			if ("p5".equals(p.getLabel())) {
				assertEquals(pres.size(), 9);
				assertNull(pros);
			}
		}

	}

}
