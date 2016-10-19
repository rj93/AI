package players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import moves.Move;
import quoridor.GameDisplay;
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
		Map<GameState2P, Integer> playedMoves = new HashMap<GameState2P, Integer>(); 
		List<Move> legalMoves = GameState2P.getLegalMoves(state, index);
        long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        try {
	        while  (System.currentTimeMillis() < stopTime){
	        	Move move = legalMoves.get(random.nextInt(legalMoves.size()));
	        	GameState2P next = move.doMove(state);
	        	
	        	int wins = playedMoves.containsKey(next) ? playedMoves.get(next) : 0;
	        	if (playRandomGame(next, stopTime, indexOpponent))
	        		wins++;
	        	playedMoves.put(next, wins);
	        }
        } catch (Exception e){
        	System.err.println(e.getMessage());
        }
		
		int max = -1;
		GameState2P state = null;
		for (Map.Entry<GameState2P, Integer> entry : playedMoves.entrySet()){
			if (max < entry.getValue()){
				max = entry.getValue();
				state = entry.getKey();
			}
		}
		System.out.println("max wins " + max);
		
		game.doMove(index, state);
	}
	
	private boolean playRandomGame(GameState2P s, long timeup, int playerIndex){
		
		if (s.isGameOver()){
			if (s.isWinner(index)){
//				GameDisplay testDisplay = new GameDisplay(s);
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				testDisplay.dispose();
			}
			return s.isWinner(index);
		} else if (System.currentTimeMillis() < timeup){
			if (s.isGameOver()){
				System.out.println("game is over but in else");
			}
			List<Move> legalMoves = GameState2P.getLegalMoves(s, playerIndex);
			Move move = legalMoves.get(random.nextInt(legalMoves.size()));
        	GameState2P next = move.doMove(s);
        	return playRandomGame(next, timeup, (playerIndex + 1) % 2);
		}

		return false;
	}

}
