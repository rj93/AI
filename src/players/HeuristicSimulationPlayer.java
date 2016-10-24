package players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class HeuristicSimulationPlayer extends AlphaBetaPlayerFixed {
	
    private long availableTime=5000;
    private int indexOpponent;

	public HeuristicSimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
		indexOpponent = (index + 1) % 2;
	}
	
	public void doMove(){
		Map<GameState2P, Integer> playedMoves = new HashMap<GameState2P, Integer>(); 
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        while  (System.currentTimeMillis() < stopTime){
        	double prob = random.nextDouble();
    		Move move = getMove();
    		GameState2P next = move.doMove(state);
    		int wins = playedMoves.containsKey(next) ? playedMoves.get(next) : 0;
    		if (playGame(next, stopTime, indexOpponent))
	        	wins++;
	        playedMoves.put(next, wins);
        }
        

//		GameState2P newState = move.doMove(state);
//        game.doMove(index, newState);
		
	}
	
	public boolean playGame(GameState2P s, long timeup, int playerIndex){
		
		if (s.isGameOver()){
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < timeup){
			Move move = getMove();
			GameState2P next = move.doMove(s);
			return playGame(next, timeup, (playerIndex + 1) % 2);
		}
		
		return false;
		
	}
	
	public Move getMove(){
		double prob = random.nextDouble();
		Move move = null;
		if (prob < 0.9){
			move = chooseMove();
		} else {
			List<Move> legalMoves = GameState2P.getLegalMoves(state, index);
			move = legalMoves.get(random.nextInt(legalMoves.size()));
		}
		return move;
	}
	
	

}
