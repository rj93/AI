package players;

import quoridor.GameState2P;
import quoridor.Quoridor;

public class RecursiceUCB1SimulationPlayer extends UCB1SimulationPlayer {
	
	private int depth = 2;

	public RecursiceUCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public RecursiceUCB1SimulationPlayer(GameState2P state, int index, Quoridor game, int depth) {
		super(state, index, game);
		this.depth = depth;
	}
	
	@Override
	public void doMove(){
		
	}
	
	private boolean runSimulation(GameState2P s, long stopTime, int playerIndex, int depth){
		if (s.isGameOver()){
			return s.isWinner(index);
		} else {
			if (depth == 0){
				return runSimulation(s, stopTime, playerIndex);
			} else {
				// run UCB
			}
		}
		return false;
	}

}
