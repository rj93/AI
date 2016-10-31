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
	
	private long availableTime = 5000;
//	private Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>(); 
	private int totalSims = 0;
	
	public UCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}

	@Override
	public void doMove() {
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>(); 
		totalSims = 0;
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        List<GameState2P> legalStates = new ArrayList<GameState2P>();
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
//        			boolean win = playGame(s, stopTime, index);
//        			playedMoves.put(s, Arrays.asList(1, (win) ? 1 : 0));
        		}
        	}
        	if (!unplayedMove){
        		double ubc1 = -1;
        		int n = 0, w = 0;
        		List<GameState2P> l = new ArrayList<GameState2P>();
        		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
        			int tempN = entry.getValue().get(0);
        			int tempW = entry.getValue().get(1);
        			double tempUBC1 = calcUBC1(tempN, tempW);
        			if (ubc1 < tempUBC1){
        				ubc1 = tempUBC1;
        				n = tempN;
        				w = tempW;
//	        				stateToPlay = entry.getKey();
        				l.clear();
        				l.add(entry.getKey());
        			} else if (ubc1 == tempUBC1){
        				l.add(entry.getKey());
        			}
        			
        			stateToPlay = l.get(random.nextInt(l.size()));
        		}
//        		System.out.println(stateToPlay + " = " + ubc1 + " (n = " + n + ", w = " + w + ")");
        	}
        	int n = 1;
        	int w = (playGame(stateToPlay, stopTime, index)) ? 1 : 0;
        	if (playedMoves.containsKey(stateToPlay)){
    			n = playedMoves.get(stateToPlay).get(0) + 1;
    			w = playedMoves.get(stateToPlay).get(1) + w;
    		}
        	
        	playedMoves.put(stateToPlay, Arrays.asList(n, w));
        	
        }
//        System.out.println("simulations = " + counter + ", total = " + totalSims);
        double percentage = 0.0;
		GameState2P state = null;
		List<GameState2P> l = new ArrayList<GameState2P>();
		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
			int plays = entry.getValue().get(0);
			int wins = entry.getValue().get(1);
			double tempPercentage = (wins / (double) totalSims) * 100;
			System.out.println(entry.getKey() + " " + tempPercentage + "% " + "wins=" + wins + " plays=" + plays);
			if (percentage < tempPercentage){
				percentage = tempPercentage;
				l.clear();
				l.add(entry.getKey());
			} else if (percentage == tempPercentage){
				l.add(entry.getKey());
			}
			
		}
		state = l.get(random.nextInt(l.size()));
		System.out.println(state + " = " + percentage + "%\n");
		game.doMove(index, state);
		
	}
	
	private double calcUBC1(int n, int w){
		
		double value = w / (double) n + Math.sqrt( (2 * Math.log(totalSims)) / (double) n );
//		System.out.println("n = " + n + " w = " + w + " UBC1 score = " + value);
		return value;
	}
	
}
