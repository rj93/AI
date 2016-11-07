package players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class RandomSimulationPlayer extends SimulationPlayer {
    
    public RandomSimulationPlayer(GameState2P state, int index, Quoridor game) {
        super(state, index, game);
    }
    
    public RandomSimulationPlayer(GameState2P state, int index, Quoridor game, long availableTime) {
		super(state, index, game, availableTime);
	}

	@Override
	public void doMove() {
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>();
		
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
        while  (System.currentTimeMillis() < stopTime){
        	Move move = getRandomMove(state, index);
        	GameState2P next = move.doMove(state);
        	
        	int n = 1;
        	int w = (runSimulation(next, stopTime, (index+1)%2)) ? 1 : 0;
        	if (playedMoves.containsKey(next)){
    			n = playedMoves.get(next).get(0) + 1;
    			w = playedMoves.get(next).get(1) + w;
    		}
        	
        	playedMoves.put(next, Arrays.asList(n, w));
        }
		
        GameState2P state = getBestState(playedMoves);
		game.doMove(index, state);
	}
	
	private boolean runSimulation(GameState2P s, long timeup, int playerIndex){
		
		if (s.isGameOver()){
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < timeup){
			Move move = getRandomMove(s, playerIndex);
        	GameState2P next = move.doMove(s);
        	return runSimulation(next, timeup, (playerIndex + 1) % 2);
		}

		return false;
	}
	
	protected Move getRandomMove(GameState2P s, int playerIndex){
		List<Move> legalMoves = GameState2P.getLegalMoves(s, playerIndex);
		return legalMoves.get(random.nextInt(legalMoves.size()));
	}

}
