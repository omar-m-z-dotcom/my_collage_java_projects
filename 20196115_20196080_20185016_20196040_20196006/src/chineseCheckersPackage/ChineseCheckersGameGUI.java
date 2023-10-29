package chineseCheckersPackage;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

@SuppressWarnings("serial")
public class ChineseCheckersGameGUI extends JFrame {
   public static final int[] array1 = {1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1};
 
   public static final int ROWS = 17;
   public static final int COLS = 13;
   public static final int CELL_SIZE = 40;
   public static final int SCREEN_WIDTH = CELL_SIZE * COLS;
   public static final int SCREEN_HEIGHT = CELL_SIZE * ROWS;
   public static final int GRIDS_WIDTH = 4;
   public static final int GRIDS_WIDHT_HALF = GRIDS_WIDTH / 2;
   public static final int CELL_BORDER = CELL_SIZE / 6;
   public static final int SYMBOL_SIZE = CELL_SIZE - CELL_BORDER * 2;
   public static final int SYMBOL_STROKE_WIDTH = 4;
   
   public static Seed Movedpieces;
   public static boolean firstClick = true;
   public static int[] firstMove = new int[2];
   public static Node pieceMove = null;

   public enum GameState {
      PLAYING, CROSS_WON, NOUGHT_WON
   }
   private GameState currentplay;
 
   public enum Seed {
      EMPTY,  CROSS, CROSS0, CROSS1, CROSS2, CROSS3, CROSS4, CROSS5, CROSS6, CROSS7, CROSS8, CROSS9, 
      NOUGHT, NOUGHT0, NOUGHT1, NOUGHT2, NOUGHT3, NOUGHT4, NOUGHT5, NOUGHT6, NOUGHT7, NOUGHT8, NOUGHT9, CONSIDERED, ILLEGITIMATE
   }
   
   public static Seed[] crossId = {Seed.CROSS0, Seed.CROSS1, Seed.CROSS2, Seed.CROSS3, Seed.CROSS4, Seed.CROSS5, 
		   Seed.CROSS6, Seed.CROSS7, Seed.CROSS8, Seed.CROSS9};
   
   public static Seed[] noughtId = {Seed.NOUGHT0, Seed.NOUGHT1, Seed.NOUGHT2, Seed.NOUGHT3, Seed.NOUGHT4, Seed.NOUGHT5, 
		   Seed.NOUGHT6, Seed.NOUGHT7, Seed.NOUGHT8, Seed.NOUGHT9};
   
   private Seed HumanPlayer;
 
   private Seed[][] ScreenBoard;
   private Drawtool Screen;
   private JLabel statusBar;
 
   
   public ChineseCheckersGameGUI() {
      Screen = new Drawtool();
      Screen.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
      
      Screen.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
             int mouseX_pos = e.getX();
             int mouseY_pos = e.getY();
             
             int Selectedrow = mouseY_pos / CELL_SIZE;
             int Selectedcol = mouseX_pos / CELL_SIZE;;
             if (Selectedrow%2 == 1) {
                 double column = (mouseX_pos - CELL_SIZE/2) / CELL_SIZE;
                 Selectedcol = (int) column;
             }

      		Simulation game_judge = new Simulation(ScreenBoard);
             if (currentplay == GameState.PLAYING) {
             	if(firstClick) {
             		Movedpieces = checkPiece(ScreenBoard, HumanPlayer, Selectedrow, Selectedcol);
             		
             		if (checkPresent(crossId, Movedpieces) || checkPresent(noughtId, Movedpieces)) {
             			ArrayList<Integer> moves = game_judge.legalMoves(ScreenBoard, HumanPlayer, Selectedrow, Selectedcol);
             			thinkAboutrMoves(moves);
             			
             			firstMove[0] = Selectedrow;
             			firstMove[1] = Selectedcol;
                 		firstClick = false;
             		} else {
             			Movedpieces = Seed.ILLEGITIMATE;
             			firstClick = true;
             		}
             		
             	} else {
                    statusBar.setText("Computer's Turn");
         			deConsiderMoves();
         			ArrayList<Integer> moves = game_judge.legalMoves(ScreenBoard, HumanPlayer, firstMove[0], firstMove[1]);
                 	if (isLegalMove(moves, Selectedrow, Selectedcol)) {
                 		removePiece(Movedpieces);
                 		ScreenBoard[Selectedrow][Selectedcol] = Movedpieces;
                 		int[] finalMove = {Selectedrow, Selectedcol};
        		        currentplay = game_judge.updateGameState(ScreenBoard, HumanPlayer);
        		        HumanPlayer = (HumanPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;            	

                        repaint();
                         
                         if (currentplay == GameState.PLAYING) {
                         	pieceMove = AIMove(pieceMove, firstMove, finalMove);
                         }
                 	} 
                 	Movedpieces = Seed.ILLEGITIMATE;
                     firstClick = true;
             	}
             } else {
                initGame();
             }
             
             repaint();
          }
       });
      
      statusBar = new JLabel("  ");
      statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
      statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
 
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      cp.add(Screen, BorderLayout.CENTER);
      cp.add(statusBar, BorderLayout.PAGE_END);
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setTitle("Chinese Checkers");
      setVisible(true);
 
      ScreenBoard = new Seed[ROWS][COLS];
      initGame();
   }
   
   	public void initGame() {
       int col;
       for (int row = 0; row < ROWS; row++) {
      	 col = ((COLS-1)/2) - ((array1[row] - (array1[row]%2))/2);
      	 for (int i = 0; i < array1[row]; i++) {
      		 ScreenBoard[row][col] = Seed.EMPTY;
      		 col++;
      	 }
      	 col = 0;
       }

       for (int row = 0; row < ROWS; row++) {
    	   for (col = 0; col < COLS; col++) {
    		   if (ScreenBoard[row][col] != Seed.EMPTY) {
          		 ScreenBoard[row][col] = Seed.ILLEGITIMATE;
    		   }  
    	   }
       }

	   int count = 0;
       for (int row = 0; row < 4; row++) {
        	 col = ((COLS-1)/2) - ((array1[row] - (array1[row]%2))/2);
        	 for (int i = 0; i < array1[row]; i++) {
        		 ScreenBoard[row][col] = noughtId[count];
        		 col++;
            	 count++;
        	 }
          	 col = 0;
       }
       
       count = 0;
       for (int row = ROWS - 1; row > (ROWS - 5); row--) {
        	 col = ((COLS-1)/2) - ((array1[row] - (array1[row]%2))/2);
      	 	for (int i = 0; i < array1[row]; i++) {
      	 		ScreenBoard[row][col] = crossId[count];
      	 		col++;
          	 	count++;
      	 	}
         	 col = 0;
        }
      currentplay = GameState.PLAYING;
      HumanPlayer = Seed.CROSS;
   }


	public Seed checkPiece(Seed[][] boarding, Seed player, int rowSelected, int colSelected) {
		Seed selected = Seed.ILLEGITIMATE;
		if (player == Seed.CROSS) {
			   for (int i = 0; i < crossId.length; i++) {
				   if (boarding[rowSelected][colSelected] == crossId[i]) {
					   selected = crossId[i];
				   }
			   }
		} else if (player == Seed.NOUGHT) {
			   for (int i = 0; i < noughtId.length; i++) {
				   if (boarding[rowSelected][colSelected] == noughtId[i]) {
					   selected = noughtId[i];
				   }
			   }
		}
		return selected;
	}
	
	public void removePiece(Seed piece) {
		for (int row = 0; row < ScreenBoard.length; row++) {
			for (int col = 0; col < ScreenBoard[0].length; col++) {
				if (piece == ScreenBoard[row][col]) {
					ScreenBoard[row][col] = Seed.EMPTY;
				}
			}
		}
	}
	
   
   public boolean isLegalMove(ArrayList<Integer> moves, int rowSelected, int colSelected) {
		boolean possibility = false;
		int possible = moves.size()/2;
		for (int look = 0; look < possible; look++) {
			if ((rowSelected == moves.get(look*2)) && (colSelected == moves.get(look*2 + 1))) {
				possibility = true;
				break;
			}
		}
		return possibility;
	}

	   public void thinkAboutrMoves(ArrayList<Integer> moves) {
		   ArrayList<Integer> movesC = moves;
		   int count = movesC.size()/2;
		   for (int i = 0; i < count; i++) {
			   ScreenBoard[movesC.get(i*2)][movesC.get((i*2)+1)] = Seed.CONSIDERED;
		   }
	   }
	   
	   public void deConsiderMoves() {
		   for (int row = 0; row < ScreenBoard.length; row ++) {
			   for (int col = 0; col < ScreenBoard[0].length; col++) {
				   if (ScreenBoard[row][col] == Seed.CONSIDERED) {
					   ScreenBoard[row][col] = Seed.EMPTY;
				   }
			   }
		   }
	   }

   public Node AIMove(Node moveNode, int[] moveFirst, int[] moveLast) {
       Simulation sim = new Simulation(ScreenBoard);
       int[] move = {moveFirst[0], moveFirst[1], moveLast[0], moveLast[1]};
       Node AIMove = sim.simulateGame(HumanPlayer, moveNode, move);
       Seed piece = checkPiece(ScreenBoard, HumanPlayer, AIMove.getX0(), AIMove.getY0());
       removePiece(piece);
       ScreenBoard[AIMove.getX()][AIMove.getY()] = piece;
       currentplay = sim.updateGameState(ScreenBoard, HumanPlayer);         
       HumanPlayer = (HumanPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
       return AIMove;
   }
   
   public boolean checkPresent(Seed[] options, Seed match) {
	   boolean check = false;
	   for (int i = 0; i < options.length; i++) {
		   if (match == options[i]) {
			   check = true;
		   }
	   }
	   return check;
   }
 
   class Drawtool extends JPanel {
      @Override
      public void paintComponent(Graphics g) {
         super.paintComponent(g); 
         setBackground(Color.WHITE);
         g.setColor(Color.BLACK);
         
         int xVal;
         for (int yVal = 0; yVal < array1.length; yVal++) {
        	 xVal = 0;
        	 while (xVal < array1[yVal]) {
        		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yVal]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yVal]) - (array1[yVal]%2))/2)) + 
        				 ((CELL_SIZE) * xVal), CELL_SIZE * yVal, CELL_SIZE, CELL_SIZE);
        		 xVal++;
        	 }
         }
         
         
         Graphics2D g2d = (Graphics2D)g;
         g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND,
               BasicStroke.JOIN_ROUND));
         
         for (int yVal = 0; yVal < ROWS; yVal++) {
        	 for (int xFill = 0; xFill < COLS; xFill++) {
        		 int yPlot = yVal;
        		 int xPlot = (((array1[yPlot] - (array1[yPlot]%2))/2) - 6) + xFill;
        		 
        		 
        		 if (checkPresent(crossId, ScreenBoard[yVal][xFill])) {
        			 g2d.setColor(Color.RED);
            		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
            		 g.fillOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
        		 }
        		 
        		 if (checkPresent(noughtId, ScreenBoard[yVal][xFill])) {
        			 g2d.setColor(Color.BLUE);
            		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
            		 g.fillOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
        		 }

        		 if (ScreenBoard[yVal][xFill] == Seed.CONSIDERED) {
        			 g2d.setColor(Color.GREEN);
            		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
            		 g.fillOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
        		 }
        		 

        		 if (ScreenBoard[yVal][xFill] == Seed.EMPTY) {
        			 g2d.setColor(Color.WHITE);
            		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
            		 g.fillOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
        			 g2d.setColor(Color.BLACK);
            		 g.drawOval(((SCREEN_WIDTH/2) - (((array1[yPlot]) % 2) * CELL_SIZE/2)) - (CELL_SIZE * (((array1[yPlot]) - (array1[yPlot]%2))/2)) + 
            				 ((CELL_SIZE) * xPlot), CELL_SIZE * yPlot, CELL_SIZE, CELL_SIZE);
        		 }
        	 }
         }
 
         
         if (currentplay == GameState.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            if (HumanPlayer == Seed.CROSS) {
               statusBar.setText("Your Turn");
            } else {
               statusBar.setText("Computer's Turn");
            }
         } else if (currentplay == GameState.CROSS_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Red' Won! Click to play again.");
         } else if (currentplay == GameState.NOUGHT_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Blue' Won! Click to play again.");
         }
      }
   }
}
