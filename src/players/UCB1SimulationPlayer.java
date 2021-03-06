package players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class UCB1SimulationPlayer extends HeuristicSimulationPlayer {
	
	public UCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public UCB1SimulationPlayer(GameState2P state, int index, Quoridor game, long availableTime) {
		super(state, index, game, availableTime);
	}

	@Override
	public void doMove() {
		Map<GameState2P, List<Integer>> playedStates = new HashMap<GameState2P, List<Integer>>();
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
    
        List<GameState2P> legalStates = getLegalGameStates(state, index);
        
        int totalSims = 0;
        while (System.currentTimeMillis() < stopTime){
        	totalSims++;
        	
        	GameState2P stateToPlay = getUCB1State(playedStates, legalStates, totalSims);
        	int n = 1;
        	int w = (runSimulation(stateToPlay, stopTime, (index+1)%2)) ? 1 : 0;
        	if (playedStates.containsKey(stateToPlay)){
    			n = playedStates.get(stateToPlay).get(0) + 1;
    			w = playedStates.get(stateToPlay).get(1) + w;
    		}
        	playedStates.put(stateToPlay, Arrays.asList(n, w));
        	
        }

		GameState2P state = getBestState(playedStates);
		game.doMove(index, state);
		
	}
	
	protected List<GameState2P> getLegalGameStates(GameState2P state, int playerIndex){
		List<Move> legalMoves = GameState2P.getLegalMoves(state, playerIndex);
		
		List<GameState2P> legalStates = new ArrayList<GameState2P>();
        for (Move m : legalMoves){ // create list of all legal states
        	GameState2P s = m.doMove(state);
        	legalStates.add(s);
        }
        return legalStates;
	}
	
	protected GameState2P getUCB1State(Map<GameState2P, List<Integer>> playedStates, List<GameState2P> legalStates, int totalSims){
		
		boolean unplayedStates = false;
    	GameState2P stateToPlay = null;
    	for (GameState2P s: legalStates){ // check for any unplayed states
    		if (!playedStates.containsKey(s)){
    			unplayedStates = true;
    			stateToPlay = s;
    		}
    	}
    	if (!unplayedStates){ // all states have been played atleast once
    		double ucb1 = -1;
    		List<GameState2P> l = new ArrayList<GameState2P>();
    		for (Map.Entry<GameState2P, List<Integer>> entry : playedStates.entrySet()){
    			int n = entry.getValue().get(0);
    			int w = entry.getValue().get(1);
    			double tempUBC1 = calcUCB1(n, w, totalSims);
    			if (ucb1 < tempUBC1){ // if this states UCB score is better
    				ucb1 = tempUBC1;
    				l.clear(); // clear the old list
    				l.add(entry.getKey());
    			} else if (ucb1 == tempUBC1){ // if this states UCB score is the same as the highest
    				l.add(entry.getKey()); // add to the list
    			}
    		}
			stateToPlay = l.get(random.nextInt(l.size())); // choose randomly from list of states with equally highest UCB scores
    	}
		return stateToPlay;
	}
	
	protected double calcUCB1(int n, int w, int totalSims){
		double value = w / (double) n + Math.sqrt( (2 * Math.log(totalSims)) / (double) n );
//		System.out.println("n = " + n + " w = " + w + " sims =  " + totalSims + " UBC1 score = " + value);
		return value;
	}
	
}
