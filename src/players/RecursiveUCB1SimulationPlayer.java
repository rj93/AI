package players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import node.Node;
import quoridor.GameState2P;
import quoridor.Quoridor;

public class RecursiveUCB1SimulationPlayer extends UCB1SimulationPlayer {
	
	private int depth = 2;

	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game) {
		super(state, index, game);
	}
	
	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game, long availableTime) {
		super(state, index, game, availableTime);
	}
	
	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game, long availableTime, int depth) {
		super(state, index, game, availableTime);
		this.depth = depth;
	}
	
	public RecursiveUCB1SimulationPlayer(GameState2P state, int index, Quoridor game, int depth) {
		super(state, index, game);
		this.depth = depth;
	}
	
	@Override
	public void doMove(){
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
		int totalSims = 0;
		Node parent = new Node((index+1)%2, state);
		while (System.currentTimeMillis() < stopTime){
			totalSims++;
        	runSimulation(parent, stopTime, index, depth);
		}
		double percentage = 0.0;
		List<Node> l = new ArrayList<Node>();
		int childTotal = 0;
		for (Node child : parent.getChildren()){
			childTotal += child.getPlays();
			int plays = child.getPlays();
			int wins = child.getWins();
			double tempPercentage = (wins / (double) plays) * 100;
			if (parent.getChildren().size() <= 3)
				System.out.println(tempPercentage);

			if (percentage < tempPercentage){
				percentage = tempPercentage;
				l.clear();
				l.add(child);
			} else if (percentage == tempPercentage){
				l.add(child);
			}
			
		}
		
		System.out.println(totalSims + " " + parent.getPlays() + " " + childTotal + " " + parent.getChildren().size() + " " + GameState2P.getLegalMoves(state, index).size());
		System.out.println("percentage  = " + percentage);
		GameState2P state = l.get(random.nextInt(l.size())).getState();
		game.doMove(index, state);
		
	}
	
	private boolean runSimulation(Node parent, long stopTime, int playerIndex, int depth){
		if (stopTime < System.currentTimeMillis())
			return false;
		parent.incPlays();
		if (parent.getState().isGameOver()){
			return parent.getState().isWinner(index);
		} else {
			if (depth == 0){
				return runSimulation(parent.getState(), stopTime, playerIndex);
			} else {
				Node n = getBestChildNode(parent);
				parent.addChild(n);
				boolean win = runSimulation(n, stopTime, (playerIndex+1)%2, depth - 1);
//				if (parent.getIndex() != index) win = !win;
				if (win) n.incWins();
				return win;
			}
		}
	}
	
	private Node getBestChildNode(Node node){

		List<Node> l = new ArrayList<Node>();
		
		Set<Node> children = node.getChildren();
		List<GameState2P> legalStates = getLegalGameStates(node.getState(), (node.getIndex() + 1)%2);
		if (children.size() == 0){
			GameState2P stateToPlay = legalStates.get(random.nextInt(legalStates.size()));
			Node n = new Node((node.getIndex() + 1)%2, stateToPlay);
			l.add(n);
		} else if (children.size() != legalStates.size()){
			GameState2P stateToPlay = legalStates.get(random.nextInt(legalStates.size()));
			Node n = new Node((node.getIndex() + 1)%2, stateToPlay);
			while (children.contains(n)){
				n = new Node((node.getIndex() + 1)%2, stateToPlay);
			}
			l.add(n);
		} else {
//			System.out.println("children = " + children.size());
			double ucb1 = -1;
			for (Node child : children){
				int n = child.getPlays();
    			int w = child.getWins();
    			double tempUBC1 = calcUCB1(n, w, node.getPlays());
    			if (ucb1 < tempUBC1){ // if this states UCB score is better
    				ucb1 = tempUBC1;
    				l.clear(); // clear the old list
    				l.add(child);
    			} else if (ucb1 == tempUBC1){ // if this states UCB score is the same as the highest
    				l.add(child); // add to the list
    			}
			}
		}
		return l.get(random.nextInt(l.size()));
	}

}
