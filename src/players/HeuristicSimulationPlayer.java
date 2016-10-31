package players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import moves.Move;
import quoridor.GameDisplay;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class HeuristicSimulationPlayer extends AlphaBetaPlayerFixed {
	
    private long availableTime=5000;
   

	public HeuristicSimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public void doMove(){
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>();
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        int totalSims = 0;
        while  (System.currentTimeMillis() < stopTime){
        	totalSims++;
        	double prob = random.nextDouble();
    		Move move = getMove(state, index);
    		GameState2P next = move.doMove(state);
    		
    		int n = 1;
        	int w = (playGame(next, stopTime, index)) ? 1 : 0;
        	if (playedMoves.containsKey(next)){
    			n = playedMoves.get(next).get(0) + 1;
    			w = playedMoves.get(next).get(1) + w;
    		}
        	
        	playedMoves.put(next, Arrays.asList(n, w));
        }
        
        double percentage = 0.0;
		GameState2P state = null;
		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
			int plays = entry.getValue().get(0);
			int wins = entry.getValue().get(1);
			double tempPercentage = (wins / (double) totalSims) * 100;
			if (percentage < tempPercentage){
				percentage = tempPercentage;
				state = entry.getKey();
			}
		}
		System.out.println("height percentage = " + percentage);
		game.doMove(index, state);
		
	}
	
	public boolean playGame(GameState2P s, long stopTime, int playerIndex){
		if (s.isGameOver()){
//			if (!s.isWinner(index)){
//				GameDisplay gs = new GameDisplay(s);
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				gs.dispose();
//			}
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < stopTime){
			Move move = getMove(s, playerIndex);
        	GameState2P next = move.doMove(s);
        	if (next == null){
        		GameDisplay gd = new GameDisplay(s);
            	System.out.println(playerIndex);
            	System.out.println(move);
        	}
        	return playGame(next, stopTime, (playerIndex + 1) % 2);
		}
		
		return false;
		
	}
	
	public Move getMove(GameState2P s, int playerIndex){
		double prob = random.nextDouble();
		Move move = null;
		if (prob < 0.9){
			move = chooseMove(playerIndex, s);
		} else {
			List<Move> legalMoves = GameState2P.getLegalMoves(s, playerIndex);
			move = legalMoves.get(random.nextInt(legalMoves.size()));
		}
		return move;
	}
	
	

}
