/*
 * Author(s): Rajae Robinson, Sydney Chambers
 */


package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.*;

public class DefaultFrame extends JFrame {
	public DefaultFrame() {
		this.setTitle("UTECH Complaint & Query System - ADVISOR");
		this.setSize(1024, 768);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon logo = new ImageIcon("img/logo.png");
		this.setIconImage(logo.getImage());
		this.setLocationRelativeTo(null);
	}
}
