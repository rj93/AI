package players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class UCB1SimulationPlayer extends HeuristicSimulationPlayer {
	
	private long availableTime = 5000;
	private Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>(); 
	private int totalMoves = 0;
	
	public UCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}

	@Override
	public void doMove() {
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        while  (System.currentTimeMillis() < stopTime){
        	
        	List<Move> legalMoves = GameState2P.getLegalMoves(state, index);
        	boolean unplayedMove = false;
        	GameState2P stateToPlay = null;
        	for (Move m : legalMoves){
        		GameState2P s = m.doMove(state);
        		if (!playedMoves.containsKey(s)){
        			// play this move
        			unplayedMove = true;
        			stateToPlay = s;
//        			boolean win = playGame(s, stopTime, index);
//        			playedMoves.put(s, Arrays.asList(1, (win) ? 1 : 0));
        		}
        	}
        	if (!unplayedMove){
        		double ubc1 = -1;
        		int n = 0, w = 0;
        		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
        			int tempN = entry.getValue().get(0);
        			int tempW = entry.getValue().get(1);
        			double tempUBC1 = calcUBC1(tempN, tempW);
        			if (ubc1 < tempUBC1){
        				ubc1 = tempUBC1;
        				n = tempN;
        				w = tempW;
        				stateToPlay = entry.getKey();
        			}
        		}
        	}
        	boolean win = playGame(stateToPlay, stopTime, index);
        	int n = 1;
        	int w = (win) ? 1 : 0;
//        	System.out.println(win + " " + w);
    		if (playedMoves.containsKey(stateToPlay)){
    			n = playedMoves.get(stateToPlay).get(0) + 1;
    			w = playedMoves.get(stateToPlay).get(0) + w;
    		}
    		playedMoves.put(stateToPlay, Arrays.asList(n, w));
    		totalMoves++;
        	
        }
        
        int max = -1;
		GameState2P state = null;
		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
			int wins = entry.getValue().get(1);
			if (max < wins){
				max = wins;
				state = entry.getKey();
			}
		}
		System.out.println(state);
		game.doMove(index, state);
		
	}
	
	private double calcUBC1(int n, int w){
		
		double value = w / (double) n + Math.sqrt( (2 * Math.log(totalMoves)) / (double) n );
//		System.out.println("n = " + n + " w = " + w + " UBC1 score = " + value);
		return value;
	}
	
}
