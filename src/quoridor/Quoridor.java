package quoridor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import players.*;

/**
 *
 * @author steven
 */
public class Quoridor {

	public static final int ALPHA_BETA_FIXED = 0;
	public static final int ALPHA_BETA_ITERATIVE = 1;
	public static final int RANDOM = 2;
	public static final int HEURISTIC = 3;
	public static final int UCB1 = 4;
	public static final int RECURSIVE_UCB1 = 5;
	
    GameState2P state;
    QuoridorPlayer[] players;
    GameDisplay display;
    boolean showGUI = true;
    private int[] playerTypes;

    public Quoridor() {
        playerTypes = new int[]{ALPHA_BETA_FIXED, ALPHA_BETA_ITERATIVE};
        startGUIGame();
    }
    
    public Quoridor(int playerType1, int playerType2){
    	playerTypes = new int[]{playerType1, playerType2};
    	startGUIGame();
    }
   
    public void startGUIGame() {
        state = new GameState2P();
        showGUI = true;
        display = new GameDisplay(state);
        players = new QuoridorPlayer[2];
        players[0] = getPlayer(playerTypes[0], 0); // RED
        players[1] = getPlayer(playerTypes[1], 1); // GREEN

        for (int i = 0; i < 2; i++) {
            players[i].setDisplay(display);
        }
        players[0].doMove();
    }
    
    private QuoridorPlayer getPlayer(int playerType, int playerIndex){
    	QuoridorPlayer p = null;
    	
    	switch (playerType){
    	case ALPHA_BETA_FIXED:
    		p = new AlphaBetaPlayerFixed(state, playerIndex, this);
    		break;
    	case ALPHA_BETA_ITERATIVE:
    		p = new AlphaBetaPlayerIterative(state, playerIndex, this);
    		break;
    	case RANDOM:
    		p = new RandomSimulationPlayer(state, playerIndex, this);
    		break;
    	case HEURISTIC:
    		p = new HeuristicSimulationPlayer(state, playerIndex, this);
    		break;
    	case UCB1:
    		p = new UCB1SimulationPlayer(state, playerIndex, this);
    		break;
    	case RECURSIVE_UCB1:
    		p = new RecursiveUCB1SimulationPlayer(state, playerIndex, this);
    		break;
    	}
    	
    	return p;
    }
    
    public boolean isOver(){
    	return state.isGameOver();
    }
    
    public int getWinner(){
    	return state.isWinner(0) ? 0 : 1;
    }
    
    public void close(){
    	display.dispose();
    }

    public void doMove(int playerIndex, GameState2P newState) {
        state = newState;
        for (int i = 0; i < 2; i++) {
            players[i].setState(newState);
        }
        final int nextIndex = (playerIndex + 1) % 2;
        if (showGUI) {
            display.updateState(newState);
        }
        if (!newState.isGameOver()) {
            /*
             * Run the method chooseMove in a separate thread
             * to avoid the GUI from becoming unresponsive while 
             * the next move by the computer player is being computed
             */
            if (showGUI) {
                new Thread() {
                    public void run() {
                        players[nextIndex].doMove();
                    }
                }.start();
            }
            else {
                players[nextIndex].doMove();
            }
        }
        else if (showGUI) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }
    }
    
    public static String getPlayerClass(int playerType){
    	String s = "";
    	switch (playerType){
    	case ALPHA_BETA_FIXED:
    		s = "AlphaBetaPlayerFixed";
    		break;
    	case ALPHA_BETA_ITERATIVE:
    		s = "AlphaBetaPlayerIterative";
    		break;
    	case RANDOM:
    		s = "RandomSimulationPlayer";
    		break;
    	case HEURISTIC:
    		s = "HeuristicSimulationPlayer";
    		break;
    	case UCB1:
    		s = "UCB1SimulationPlayer";
    		break;
    	case RECURSIVE_UCB1:
    		s = "RecursiveUCB1SimulationPlayer";
    		break;
    	}
    	return s;
    }

    public static void main(String[] args) {
    	
    	List<Integer> players = new ArrayList<Integer>(
   			Arrays.asList(
   				Quoridor.ALPHA_BETA_FIXED, 
    			Quoridor.ALPHA_BETA_ITERATIVE, 
   				Quoridor.RANDOM,
    			Quoridor.HEURISTIC, 
    			Quoridor.UCB1,
    			Quoridor.RECURSIVE_UCB1
    		)
    	);
    		
    	for (int p1 : players){
    		for (int p2 : players){
    			if (p1 == p2) continue;
    			System.out.println("Match - Player 1 = " + Quoridor.getPlayerClass(p1) + " Player 2 = " + Quoridor.getPlayerClass(p2));
		    	int[] wins = {0, 0};
		    	for (int i = 0; i < 10; i++){
		        	Quoridor quoridor = new Quoridor(p1, p2);
			    	while (!quoridor.isOver()){
			    		try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
			    	}
			    	int winner = quoridor.getWinner();
			    	String winnerStr = Quoridor.getPlayerClass((winner == p1) ? p1 : p2);
			    	System.out.println(String.format("game %d won by player %d (%s)", i+1, winner+1, winnerStr));
			    	wins[winner]++;
			    	quoridor.close();
		    	}
//		    	System.out.println("Player 1 (" + Quoridor.getPlayerClass(p1) + ") wins = " + wins[0]);
//		    	System.out.println("Player 2 (" + Quoridor.getPlayerClass(p2) + ") wins = " + wins[1]);
    		}
    	}
    	
    }
}
