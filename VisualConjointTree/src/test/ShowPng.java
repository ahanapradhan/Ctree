package test;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowPng extends JFrame
{    
  private ShowPng(String arg){
            
    JPanel panel = new JPanel();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    panel.setSize(500,640);
  //  panel.setBackground(Color.CYAN); 
    ImageIcon icon = new ImageIcon(arg); 
    JLabel label = new JLabel(); 
    label.setIcon(icon); 
    panel.add(label);
    this.getContentPane().add(panel); 
  }
  public static void showImage(String args) {
      new ShowPng(args).setVisible(true); 
  }
}