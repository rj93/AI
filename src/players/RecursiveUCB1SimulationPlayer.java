package players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import quoridor.GameState2P;
import quoridor.Quoridor;

public class RecursiveUCB1SimulationPlayer extends UCB1SimulationPlayer {
	
	private int depth = 2;

	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game, int depth) {
		super(state, index, game);
		this.depth = depth;
	}
	
	@Override
	public void doMove(){
		Map<GameState2P, List<Integer>> playedStates = new HashMap<GameState2P, List<Integer>>();
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
		List<GameState2P> legalStates = getLegalGameStates(state, index);
		int totalSims = 0;
		while (System.currentTimeMillis() < stopTime){
        	totalSims++;
        	
        	GameState2P stateToPlay = getUCB1State(playedStates, legalStates, totalSims);
        	int n = 1;
        	int w = (runSimulation(stateToPlay, stopTime, (index+1)%2, depth)) ? 1 : 0;
        	if (playedStates.containsKey(stateToPlay)){
    			n = playedStates.get(stateToPlay).get(0) + 1;
    			w = playedStates.get(stateToPlay).get(1) + w;
    		}
        	playedStates.put(stateToPlay, Arrays.asList(n, w));
		}
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
