package players;

import java.util.List;
import java.util.Random;
import quoridor.GameState2P;
import quoridor.Quoridor;
import moves.*;

/**
 *
 * @author steven
 */
public class AlphaBetaPlayerFixedRefactored extends QuoridorPlayer {

    public static Random random = new Random();
    protected int indexOpponent;
    protected int maxDepth = 2;

    public AlphaBetaPlayerFixedRefactored(GameState2P state, int index, Quoridor game) {
        super(state, index, game);
        indexOpponent = (index + 1) % 2;
    }

    public void setMaxDepth(int maxDepth){
        this.maxDepth=maxDepth;
    }
    
    public void doMove() {
        Move m = chooseMove();
        GameState2P newState = m.doMove(state);
        game.doMove(index, newState);
    }
    
    public Move chooseMove(){
    	return chooseMove(index, state);
    }
    
    public Move chooseMove(int playerIndex, GameState2P state) {
//    	System.out.println("chooseMove " + playerIndex);
        List<Move> legalMoves = GameState2P.getLegalMoves(state, playerIndex);
        Move bestMove = null;
        double bestScore = 0;
        for (Move m : legalMoves) {
            GameState2P next = m.doMove(state);
            double score = getMinScoreAlphaBeta(next, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, (playerIndex+1)%2);
            if (bestMove == null || score > bestScore) {
                bestMove = m;
                bestScore = score;
            }
        }    
        return bestMove;
    }
    
    private double getMinScoreAlphaBeta(GameState2P s, int depth, double alpha, double beta){
    	return getMinScoreAlphaBeta(s, depth, alpha, beta, indexOpponent);
    }

    /*
     * Consider all possible moves by our opponent
     */
    private double getMinScoreAlphaBeta(GameState2P s, int depth, double alpha, double beta, int playerIndex) {
//    	System.out.println("getMinScoreAlphaBeta " + playerIndex);
        double res;
        if (depth == 0 || s.isGameOver()) {
            res = s.evaluateState(index);
        }
        else {
            List<Move> opponentMoves = GameState2P.getLegalMoves(s, playerIndex);
            res = Double.POSITIVE_INFINITY;
            for (Move move : opponentMoves) {
                GameState2P next = move.doMove(s);
                double score = getMaxScoreAlphaBeta(next, depth - 1, alpha, beta, (playerIndex+1)%2);
                res = Math.min(res, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return res;
    }
    
    private double getMaxScoreAlphaBeta(GameState2P s, int depth, double alpha, double beta){
    	return getMaxScoreAlphaBeta(s, depth, alpha, beta, index);
    }

    /*
     * Consider all possible moves we can play
     */
    private double getMaxScoreAlphaBeta(GameState2P s, int depth, double alpha, double beta, int playerIndex) {
//    	System.out.println("getMaxScoreAlphaBeta " + playerIndex);
        double res;
        if (depth == 0 || s.isGameOver()) {
            res = s.evaluateState(index);
        }
        else {
            List<Move> myMoves = GameState2P.getLegalMoves(s, playerIndex);
            res = Double.NEGATIVE_INFINITY;
            for (Move move : myMoves) {                
                GameState2P next = move.doMove(s);
                double score = getMinScoreAlphaBeta(next, depth - 1, alpha, beta, (playerIndex + 1) % 2);
                res = Math.max(res, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return res;
    }
}
