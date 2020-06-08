package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import treemodel.CNode;
import treemodel.CtreeBuilder;

public class TestUtil {
	
	public static void deleteFile(String s) {
		File f = new File(s);
		f.delete();
		
		if (f.exists()) {
			fail();
		}
	}
	
	public static void createSampleDotFile(File f, String text) {
		try {
			PrintWriter p = new PrintWriter(f);
			p.write(text);
			p.close();
		} catch (FileNotFoundException e) {
			fail();
		}
	}
	public static String readFile(File f) {
		StringBuilder sb = new StringBuilder();
		try {
			Scanner sc = new Scanner(f);		
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				sb.append(s+"\n");
			}
			sc.close();
		} catch (FileNotFoundException e) {
			fail();
		}
		return sb.toString();
	}
	@Test
	public void test_dotTest1() {

		String intext = "digraph G {\n" + 
				"    main -> parse -> execute;\n" + 
				"    main -> init;\n" + 
				"    main -> cleanup;\n" + 
				"    execute -> make_string;\n" +
				"    execute [shape=box,style=filled,color=\".7 .3 1.0\"];\n" +
				"    execute -> printf;\n" + 
				"    init -> make_string;\n" + 
				"    main -> printf;\n" + 
				"    execute -> compare;\n" + 
				"}\n";
		String dotFile = "smallgraph.dot";
		String outputFile = "ctree.png";
		File f = new File(dotFile);
		createSampleDotFile(f, intext);
		String printedText = readFile(f);
		
		assertEquals(intext, printedText);
		
		ProcessBuilder pb = new ProcessBuilder();
		runDot(dotFile, pb, outputFile);
		showDotGraphMac(pb, outputFile);
		
		//deleteFile(outputFile);
	}
	public static void runDot(String dotFile, ProcessBuilder pb, String outputFile) {
		File f;
		pb.command("/usr/local/bin/dot", "-Tpng", dotFile, "-o", outputFile);
		try {
			Process p = pb.start();
			
			int exVal = p.waitFor();
			if (exVal == 0) {
				f = new File(outputFile);
				if (!f.exists()) {
					fail();
				}
			}
			else {
				fail();
			}
		}
		catch (Exception e) {
			fail();
		}
	}
	
	public static void showDotGraphMac(ProcessBuilder pb, String outputFile) {
		pb.command("open", outputFile);
		try {
			Process p = pb.start();
			
			int exVal = p.waitFor();
			if (exVal != 0) {
				fail();
			}
		}
		catch (Exception e) {
			fail();
		}
	}
	
	public static void showDotGraphJava(ProcessBuilder pb, String outputFile) {
		ShowPng.showImage(outputFile);
	}
	
	public static String getPackets(String ecws) {
		String[] items = ecws.split(" ");
		List<Object> list = CtreeBuilder.buildPackets(items);
		StringBuilder sb = new StringBuilder();
		for (Object l : list) {
			if (l instanceof CNode) {
				CNode node = (CNode) l;
				// System.out.println(node.getLabel());
				sb.append(node.getLabel());
			} else if (l instanceof String) {
				// System.out.println((String) l);
				sb.append(" " + l + " ");
			} else {
				sb.append("-");
				// System.out.println("invalid entry in packet");
			}
		}
		return sb.toString();
	}


}
