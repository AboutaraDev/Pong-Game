import java.util.*;
import java.awt.Event.*;
import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

	GamePanel gamePanel;
	
	GameFrame() {
		
		gamePanel = new GamePanel();
		
		this.add(gamePanel);
		this.setTitle("Pong Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBackground(Color.black);
		this.pack();
		this.setVisible(true);
	}
}
