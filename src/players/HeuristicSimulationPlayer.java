package players;

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
		Map<GameState2P, Integer> playedMoves = new HashMap<GameState2P, Integer>(); 
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        while  (System.currentTimeMillis() < stopTime){
        	double prob = random.nextDouble();
    		Move move = getMove(state, index);
    		GameState2P next = move.doMove(state);
    		int wins = playedMoves.containsKey(next) ? playedMoves.get(next) : 0;
    		if (playGame(next, stopTime, indexOpponent))
	        	wins++;
	        playedMoves.put(next, wins);
        }
        
        int max = -1;
		GameState2P state = null;
		for (Map.Entry<GameState2P, Integer> entry : playedMoves.entrySet()){
			if (max < entry.getValue()){
				max = entry.getValue();
				state = entry.getKey();
			}
		}
		System.out.println("max = " + max);
		game.doMove(index, state);
		
	}
	
	public boolean playGame(GameState2P s, long stopTime, int playerIndex){
		if (s.isGameOver()){
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
