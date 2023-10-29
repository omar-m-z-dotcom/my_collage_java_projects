package chineseCheckersPackage;

import javax.swing.SwingUtilities;

public class ChineseCheckersGame {

	public static void main(String[] args) {

		// the player will always play as the red team
		// to play the game click on one of your pwans and click on any of the moves
		// availble that will be highlited in green
		// becarful the is slow so the when choose a move wait for some time until the
		// ai makes it's move
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ChineseCheckersGameGUI();
			}
		});

	}

}