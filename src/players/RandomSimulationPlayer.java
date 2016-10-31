package players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import moves.Move;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class RandomSimulationPlayer extends QuoridorPlayer {

    public static Random random = new Random();
    private int indexOpponent;    
    private long availableTime=5000;
    
    public RandomSimulationPlayer(GameState2P state, int index, Quoridor game) {
        super(state, index, game);
        indexOpponent = (index + 1) % 2;
    }

	@Override
	public void doMove() {
		Map<GameState2P, List<Integer>> playedMoves = new HashMap<GameState2P, List<Integer>>();
		
		List<Move> legalMoves = GameState2P.getLegalMoves(state, index);
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        int totalSims = 0;
        while  (System.currentTimeMillis() < stopTime){
        	totalSims++;
        	Move move = legalMoves.get(random.nextInt(legalMoves.size()));
        	GameState2P next = move.doMove(state);
        	
        	int n = 1;
        	int w = (playRandomGame(next, stopTime, index)) ? 1 : 0;
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
			System.out.println(tempPercentage);
			if (percentage < tempPercentage){
				percentage = tempPercentage;
				state = entry.getKey();
			}
		}
		System.out.println("height percentage = " + percentage);
		
		game.doMove(index, state);
	}
	
	private boolean playRandomGame(GameState2P s, long timeup, int playerIndex){
		
		if (s.isGameOver()){
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < timeup){
			List<Move> legalMoves = GameState2P.getLegalMoves(s, playerIndex);
			Move move = legalMoves.get(random.nextInt(legalMoves.size()));
        	GameState2P next = move.doMove(s);
        	return playRandomGame(next, timeup, (playerIndex + 1) % 2);
		}

		return false;
	}

}
