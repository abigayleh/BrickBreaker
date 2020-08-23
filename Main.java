package BrickBreaker;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		//To create the window frame of the game
		JFrame obj = new JFrame();
		GamePlay gameplay = new GamePlay();
		//Size of initial window
		obj.setBounds(10, 10, 700, 600);
		obj.setTitle("Breakout Game");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gameplay);
	}

}
