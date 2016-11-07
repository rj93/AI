package players;

import java.util.ArrayList;
import java.util.List;
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
	
	public void setDepth(int depth){
		this.depth = depth;
	}
	
	@Override
	public void doMove(){
		
		long startTime = System.currentTimeMillis();
        long stopTime = startTime + availableTime;
        
		Node root = new Node((index+1)%2, state);
		while (System.currentTimeMillis() < stopTime){
			runSimulation(root, stopTime, index, depth);
		}
		double percentage = 0.0;
		List<Node> l = new ArrayList<Node>();
		for (Node child : root.getChildren()){
			int plays = child.getPlays();
			int wins = child.getWins();
			
			double tempPercentage = (wins / (double) plays) * 100;
			if (percentage < tempPercentage){
				percentage = tempPercentage;
				l.clear();
				l.add(child);
			} else if (percentage == tempPercentage){
				l.add(child);
			}
		}
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
				Node n = getUCBChildNode(parent);
				boolean win = runSimulation(n, stopTime, (playerIndex+1)%2, depth - 1);
				if ((n.getIndex() != index && !win) || win) n.incWins(); // if this node is the opponent, the win boolean is for the player, so need to increment the opponents wins
				return win;
			}
		}
	}
	
	private Node getUCBChildNode(Node parent){

		List<Node> nodeList = new ArrayList<Node>();
		
		Set<Node> children = parent.getChildren();
		List<GameState2P> legalStates = getLegalGameStates(parent.getState(), (parent.getIndex() + 1)%2);
		if (children.size() != legalStates.size()){
			GameState2P stateToPlay = legalStates.get(random.nextInt(legalStates.size()));
			Node n = new Node((parent.getIndex() + 1)%2, stateToPlay);
			while (children.contains(n)){ // already simulated this move
				stateToPlay = legalStates.get(random.nextInt(legalStates.size()));
				n = new Node((parent.getIndex() + 1)%2, stateToPlay);
			}
			parent.addChild(n);
			nodeList.add(n);
		} else {
			double ucb1 = -1;
			for (Node child : children){
				int n = child.getPlays();
    			int w = child.getWins();
    			double tempUBC1 = calcUCB1(n, w, parent.getPlays());
    			if (ucb1 < tempUBC1){ // if this states UCB score is better
    				ucb1 = tempUBC1;
    				nodeList.clear(); // clear the old list
    				nodeList.add(child);
    			} else if (ucb1 == tempUBC1){ // if this states UCB score is the same as the highest
    				nodeList.add(child); // add to the list
    			}
			}
		}
		return nodeList.get(random.nextInt(nodeList.size()));
	}

}
