package game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Snake");
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();

		frame.setPreferredSize(new Dimension(GamePanel.WIDTH, GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}
