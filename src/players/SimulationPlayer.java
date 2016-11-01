package players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import quoridor.GameState2P;

public interface SimulationPlayer {
	
	Random random = null;
	
	default GameState2P getBest(Map<GameState2P, List<Integer>> playedMoves, int totalSims){
		double percentage = 0.0;
		
		List<GameState2P> l = new ArrayList<GameState2P>();
		for (Map.Entry<GameState2P, List<Integer>> entry : playedMoves.entrySet()){
			int plays = entry.getValue().get(0);
			int wins = entry.getValue().get(1);
			double tempPercentage = (wins / (double) totalSims) * 100;
//			System.out.println(entry.getKey() + " " + tempPercentage + "% " + "wins=" + wins + " plays=" + plays);
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
