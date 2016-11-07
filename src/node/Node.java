package node;

import java.util.HashSet;
import java.util.Set;

import quoridor.GameState2P;

public class Node {
	
	private int index;
	private Set<Node> children = new HashSet<Node>();
	private GameState2P state;
	private int plays = 0;
	private int wins = 0;
	
	public Node(int index, GameState2P state){
		this.index = index;
		this.state = state;
	}
	
	public int getIndex(){
		return index;
	}
	
	public Set<Node> getChildren(){
		return children;
	}
	
	public void addChild(Node child){
		children.add(child);
	}
	
	public GameState2P getState(){
		return state;
	}
	
	public int getPlays(){
		return plays;
	}
	
	public void incPlays(){
		plays++;
	}
	
	public int getWins(){
		return wins;
	}
	
	public void incWins(){
		wins++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (index != other.index)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	
	
	
}
