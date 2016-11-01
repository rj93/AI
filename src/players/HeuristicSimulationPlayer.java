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

public class HeuristicSimulationPlayer extends QuoridorPlayer {
	
    private long availableTime=5000;
//    private Random random = new Random();
   

	public HeuristicSimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public void doMove(){
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>();
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        while  (System.currentTimeMillis() < stopTime){
    		Move move = getMove(state, index);
    		GameState2P next = move.doMove(state);
    		
    		int n = 1;
        	int w = (playGame(next, stopTime, (index+1)%2)) ? 1 : 0;
        	if (playedMoves.containsKey(next)){
    			n = playedMoves.get(next).get(0) + 1;
    			w = playedMoves.get(next).get(1) + w;
    		}
        	
        	playedMoves.put(next, Arrays.asList(n, w));
        }
        
        GameState2P state = getBestState(playedMoves);
		game.doMove(index, state);
		
	}
	
	public boolean playGame(GameState2P s, long stopTime, int playerIndex){
		if (s.isGameOver()){
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < stopTime){
			Move move = getMove(s, playerIndex);
        	GameState2P next = move.doMove(s);
        	return playGame(next, stopTime, (playerIndex + 1) % 2);
		}
		
		return false;
		
	}
	
	public Move getMove(GameState2P s, int playerIndex){
		double prob = random.nextDouble();
		Move move = null;
		if (prob < 0.9){
			AlphaBetaPlayerFixed abpf = new AlphaBetaPlayerFixed(s, playerIndex, game);
			move = abpf.chooseMove();
		} else {
			List<Move> legalMoves = GameState2P.getLegalMoves(s, playerIndex);
			move = legalMoves.get(random.nextInt(legalMoves.size()));
		}
		return move;
	}
	
	

}
