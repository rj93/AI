package players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import quoridor.GameState2P;
import quoridor.Quoridor;

public abstract class SimulationPlayer extends QuoridorPlayer {

	protected long availableTime = 5000;
    protected Random random = new Random();
	
	public SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public SimulationPlayer(GameState2P state, int index, Quoridor game, long availableTime) {
		super(state, index, game);
		this.availableTime = availableTime;
	}

    protected GameState2P getBestState(Map<GameState2P, List<Integer>> playedStates){
    	
    	double percentage = 0.0;
		List<GameState2P> l = new ArrayList<GameState2P>();
		for (Map.Entry<GameState2P, List<Integer>> entry : playedStates.entrySet()){
			int plays = entry.getValue().get(0);
			int wins = entry.getValue().get(1);
			double tempPercentage = (wins / (double) plays) * 100;

			if (percentage < tempPercentage){
				percentage = tempPercentage;
				l.clear();
				l.add(entry.getKey());
			} else if (percentage == tempPercentage){
				l.add(entry.getKey());
			}
			
		}
		return l.get(random.nextInt(l.size()));
    }
}
