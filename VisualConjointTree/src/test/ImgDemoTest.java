package test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImgDemoTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void test_main() {
		try {
			displayPNGimage(TestUtils.PROJECT_PARENT_DIR + "petrinet.png");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void displayPNGimage(final String imgfilename) throws Exception {
		JFrame editorFrame = new JFrame(imgfilename);
		editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(imgfilename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageIcon imageIcon = new ImageIcon(image);
		JLabel jLabel = new JLabel();
		jLabel.setIcon(imageIcon);
		editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

		editorFrame.pack();
		editorFrame.setLocationRelativeTo(null);
		editorFrame.setVisible(true);
		
		TimeUnit.SECONDS.sleep(3);
	}

}
