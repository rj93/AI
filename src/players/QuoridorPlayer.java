package players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import quoridor.GameDisplay;
import quoridor.GameState2P;
import quoridor.Quoridor;

/**
 *
 * @author steven
 */
public abstract class QuoridorPlayer {
    
    protected GameState2P state;
    protected GameDisplay display;    
    protected Quoridor game;
    int index;
    protected Random random = new Random();
        
    public QuoridorPlayer(GameState2P state, int index, Quoridor game){
        this.state=state;                             
        this.index = index;
        this.game=game;
    }     
 
    public void setState(GameState2P state){
        this.state=state;
    }
    
    public int getIndex(){
        return index;
    }
    
   public void setDisplay(GameDisplay display) {
        this.display = display;        
    }
    
    public abstract void doMove();
    
    public GameState2P getBestState(Map<GameState2P, List<Integer>> playedStates){
    	
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
