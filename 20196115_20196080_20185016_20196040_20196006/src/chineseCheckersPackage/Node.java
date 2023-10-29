package chineseCheckersPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chineseCheckersPackage.ChineseCheckersGameGUI.Seed;

public class Node {
    private Node parent;
    private List<Node> childStatesArray;
    private Seed[][] gameBoard;
    private Seed player;
	private Seed piece;
    private int wins;
    private int plays;
    private int x0, y0;
    private int x, y;
    private int gameLevel;

    public Node(Node parent, Seed[][] board, Seed player, Seed piece, int wins, int plays, int x0, int y0, int x, int y) {
        this.parent = parent;
    	Simulation figure = new Simulation(board);
        this.gameBoard = figure.copyGameBoard(board);
        this.player = player;
        this.piece = piece;
        this.wins = wins;
        this.plays = plays;
        this.x0 = x0;
        this.y0 = y0;
        this.x = x;
        this.y = y;
        this.gameLevel = parent.gameLevel++;
        childStatesArray = new ArrayList<>();
    }

    public Node(Seed[][] board, Seed player, int wins, int plays, int x0, int y0, int x, int y, int level) {
    	Simulation simulationFigure = new Simulation(board);
        this.gameBoard = simulationFigure.copyGameBoard(board);
        this.player = player;
        this.wins = wins;
        this.plays = plays;
        this.x0 = x0;
        this.y0 = y0;
        this.x = x;
        this.y = y;
        this.gameLevel = level;
        childStatesArray = new ArrayList<>();
        
    }

    public Node(Node node) {
        if (node.getParent() != null) {
            this.parent = node.getParent();
        }
        this.childStatesArray = new ArrayList<>();
        ArrayList<Node> childArray = new ArrayList<Node>(node.getChildStateArray().size());
        for (int i = 0; i < node.getChildStateArray().size(); i++) {
        	childArray.add(node.getChildStateArray().get(i));
        }
        
        this.gameBoard = node.getBoard();
    	Simulation figure = new Simulation(gameBoard);
        this.gameBoard = figure.copyGameBoard(node.getBoard());
        this.player = node.getPlayer();
        this.piece = node.getPiece();
        this.wins = node.getWins();
        this.plays = node.getPlays();
        this.x0 = node.getX0();
        this.y0 = node.getY0();
        this.x = node.getX();
        this.y = node.getY();
        this.gameLevel = node.getGameLevel();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildStateArray() {
        return childStatesArray;
    }

    public void setChildArray(List<Node> childArray) {
        this.childStatesArray = childArray;
    }

    Seed[][] getBoard() {
        return gameBoard;
    }

    void setBoard(Seed[][] board) {
        this.gameBoard = board;
    }

    Seed getPlayer() {
        return player;
    }

    void setPlayer(Seed player) {
        this.player = player;
    }

    Seed getPiece() {
        return piece;
    }

    void setPiece(Seed piece) {
        this.piece = piece;
    }

    Seed getOpponent() {
        return ((player == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }
    
    public int getX0() {
        return x0;
    }
    
    public int getY0() {
        return y0;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }

    public int getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(int level) {
		this.gameLevel = level;
	}

	public Node getRandomChildNode() {
        int numberOfPossibleMoves = this.childStatesArray.size();
        int selectRandomChild = (int) (Math.random() * ((numberOfPossibleMoves - 1) + 1));
        return this.childStatesArray.get(selectRandomChild);
    }

    public Node getChildWithMaxScore() {
        return Collections.max(this.childStatesArray, Comparator.comparing(c -> {
            return c.getPlays();
        }));
    }


    public void addChild(Node parent, Node child) {
        parent.getChildStateArray().add(child);
    }
    
    public void createChildren(Node parenterNode) {
        List<Node> childrenNodes = new ArrayList<Node>();
        
    	Simulation moveFigure = new Simulation(gameBoard); 
        int[] positions = moveFigure.searchPieces(gameBoard, getOpponent());
        
        for (int i = 0; i < positions.length/2; i++) {
        	List<Integer> temporaryPositions = new ArrayList<Integer>();
        	temporaryPositions.addAll(moveFigure.legalMoves(gameBoard, getOpponent(), positions[(i*2)], positions[(i*2)+1]));
            int count = temporaryPositions.size()/2;
            for (int j = 0; j < count; j++) {
            	Seed[][] tempBoard = moveFigure.copyGameBoard(gameBoard);
            	Seed tempSeed = tempBoard[positions[i*2]][positions[i*2+1]];
            	tempBoard[positions[i*2]][positions[i*2+1]] = Seed.EMPTY;
            	tempBoard[temporaryPositions.get(j*2)][temporaryPositions.get(j*2+1)] = tempSeed;
                childrenNodes.add(new Node(parenterNode, tempBoard, getOpponent(), tempSeed, 0, 0, positions[i*2], positions[i*2+1], 
                		temporaryPositions.get(j*2), temporaryPositions.get(j*2+1)));
            }
        }
        childStatesArray = childrenNodes;
    }

    void incrementWins() {
    	this.wins++;
    }
    
    void incrementPlays() {
        this.plays++;
    }
}