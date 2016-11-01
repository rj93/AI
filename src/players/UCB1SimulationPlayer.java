package players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class UCB1SimulationPlayer extends HeuristicSimulationPlayerRefactored {
	
	private long availableTime = 5000;
//	private Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>(); 
//	private int totalSims = 0;
	
	public UCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}

	@Override
	public void doMove() {
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>();
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        List<GameState2P> legalStates = new ArrayList<GameState2P>();
        int totalSims = 0;
        while (System.currentTimeMillis() < stopTime){
        	totalSims++;
        	List<Move> legalMoves = GameState2P.getLegalMoves(state, index);
        	
        	boolean unplayedMove = false;
        	GameState2P stateToPlay = null;
        	for (Move m : legalMoves){
        		GameState2P s = m.doMove(state);
        		legalStates.add(s);
        		if (!playedMoves.containsKey(s)){
        			// play this move
        			unplayedMove = true;
        			stateToPlay = s;
        		}
        	}
        	if (!unplayedMove){
        		double ubc1 = -1;
        		int n = 0, w = 0;
        		List<GameState2P> l = new ArrayList<GameState2P>();
        		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
        			int tempN = entry.getValue().get(0);
        			int tempW = entry.getValue().get(1);
        			double tempUBC1 = calcUBC1(tempN, tempW, totalSims);
        			if (ubc1 < tempUBC1){
        				ubc1 = tempUBC1;
        				n = tempN;
        				w = tempW;
        				l.clear();
        				l.add(entry.getKey());
        			} else if (ubc1 == tempUBC1){
        				l.add(entry.getKey());
        			}
        		}
    			stateToPlay = l.get(random.nextInt(l.size()));
        	}
        	int n = 1;
        	int w = (playGame(stateToPlay, stopTime, index)) ? 1 : 0;
        	if (playedMoves.containsKey(stateToPlay)){
    			n = playedMoves.get(stateToPlay).get(0) + 1;
    			w = playedMoves.get(stateToPlay).get(1) + w;
    		}
        	playedMoves.put(stateToPlay, Arrays.asList(n, w));
        	
        }

		GameState2P state = getBestState(playedMoves);
		game.doMove(index, state);
		
	}
	
	private double calcUBC1(int n, int w, int totalSims){
		double value = w / (double) n + Math.sqrt( (2 * Math.log(totalSims)) / (double) n );
//		System.out.println("n = " + n + " w = " + w + " UBC1 score = " + value);
		return value;
	}
	
}
