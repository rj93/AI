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
}
