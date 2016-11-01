package quoridor;

import players.*;

/**
 *
 * @author steven
 */
public class Quoridor {

    GameState2P state;
    QuoridorPlayer[] players;
    GameDisplay display;
    boolean showGUI = true;

    public Quoridor() {
        startGUIGame();
    }
   
    public void startGUIGame() {
        state = new GameState2P();
        showGUI = true;
        display = new GameDisplay(state);
        players = new QuoridorPlayer[2];
//        players[0] = new HumanPlayer(state, 0, this); // RED
//        players[0] = new RandomSimulationPlayer(state, 0, this); // RED
        players[0] = new UCB1SimulationPlayer(state, 0, this); // RED
        players[1] = new AlphaBetaPlayerIterative(state, 1, this); // GREEN

        for (int i = 0; i < 2; i++) {
            players[i].setDisplay(display);
        }
        players[0].doMove();
    }
    
    public boolean isOver(){
    	return state.isGameOver();
    }
    
    public int getWinner(){
    	return state.isWinner(0) ? 0 : 1;
    }
    
    public Class<? extends QuoridorPlayer> getPlayerClass(int index){
    	return players[index].getClass();
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

    public static void main(String[] args) {
    	int[] wins = {0, 0};
    	for (int i = 0; i < 10; i++){
        	Quoridor quoridor = new Quoridor();
	    	while (!quoridor.isOver()){
	    		try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    	int winner = quoridor.getWinner();
	    	System.out.println(String.format("game %d won by player %d (%s)", i, winner, quoridor.getPlayerClass(winner).getName()));
	    	wins[winner]++;
	    	quoridor.close();
    	}
    	System.out.println("Player 0 wins = " + wins[0]);
    	System.out.println("Player 1 wins = " + wins[1]);
    }
}
