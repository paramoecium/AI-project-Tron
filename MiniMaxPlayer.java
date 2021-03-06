/**
 * MiniMax class
 *
 * originally written spring-2006/sklar for teaching
 *
 * ATTENTION STUDENTS:
 * you need to modify the method called "whereDoIGo()", below
 *
 */

import java.awt.*;
import java.util.Random;
import java.util.*;



public class MiniMaxPlayer extends MyPlayer {


    public boolean didsomething = false;
    public Random random;


    /**
     * MyPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public MiniMaxPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
    	super(n, c, a, x, y, number); 
		random = new Random();
    } /* end of MyPlayer constructor */



    /**
     * whereDoIGo
     *
     * this is the method you need to modify.
     * you need to decide where your robot should go at each time step
     * (this method is called each time the robot can make a move).
     *
     * there are constants defined in Tron.java for each of the four
     * possible directions that the robot can go:
     *  public static final int NORTH = 2;
     *  public static final int EAST  = 1;
     *  public static final int SOUTH = 0;
     *  public static final int WEST  = 3;
     *
     * this method has to return one of those values.
     * it is up to you to decide, based on the state of the board and
     * everything you have learned about AI, which of these four values to
     * return each time this method is called.
     *
     */
    public int whereDoIGo() {
		
		Playerstate currentState = getCurrentState ();
		ArrayList<Integer> actions = new ArrayList<Integer> ();
		double score = alphaBetaSearch(currentState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 12, actions);
		if(actions.size() != 0)
			return actions.get(actions.size()-1);
		else{
			System.err.println("Oops!");
			return d;
		}


    } /* end of whereDoIGo() */


	public double minimaxSearch(Playerstate currentState, int currentDepth, ArrayList<Integer> path) 
	{

		if(currentState.isGoal() || currentDepth == 0) {
			return (double)(currentState.utility(this));
		}
		//System.out.println(currentDepth);
		//currentState.printBoard();
		//System.out.println("\n");

		ArrayList<Integer> legalMoves = currentState.getLegalMoves();
		if( legalMoves.size() == 0)
			legalMoves.add(d);

		ArrayList<Playerstate> successors = new ArrayList<Playerstate> () ;
		ArrayList<Double> scores = new ArrayList<Double>() ;
		for(int move: legalMoves){
			successors.add(currentState.getSuccessor(move));
		}
		for(Playerstate successor: successors){
			scores.add(minimaxSearch(successor, currentDepth-1, path));
		}
		if(scores.size() == 0)
			System.err.println("OMG!! " + currentDepth);
		if(currentState.currentPlayer == this){
			double max = Collections.max(scores);
			int idx = scores.indexOf(max);
			path.add(legalMoves.get(idx));
			return max;
		}
		else{
			double sum = 0;
			for(Double d : scores)
				sum += d;
			return sum/scores.size();
		}
	}
	
	public double alphaBetaSearch(Playerstate currentState, double alpha, double beta, int currentDepth, ArrayList<Integer> path) 
	{

		if(currentState.isGoal() || currentDepth == 0) {
			//return (double)(currentState.utility(this));
			return (double)(currentState.evaluation(this));
		}
		
		//System.out.println(currentDepth);
		//currentState.printBoard();
		//System.out.println("\n");
		
			ArrayList<Integer> legalMoves = currentState.getShuffledLegalMoves();
			if( legalMoves.size() == 0)
				legalMoves.add(d);
	
			ArrayList<Playerstate> successors = new ArrayList<Playerstate> () ;
			ArrayList<Double> scores = new ArrayList<Double>() ;
			for(int move: legalMoves){
				successors.add(currentState.getSuccessor(move));
			}
		if(currentState.currentPlayer == this){
			double v = Double.NEGATIVE_INFINITY;
			for(Playerstate successor: successors){
				double result = alphaBetaSearch(successor, alpha, beta, currentDepth-1, path);//full re-search
				scores.add(result);
				v = Math.max(v, result);
				if (v >= beta) break;
				alpha = Math.max(alpha, v);
			}
			int idx = scores.indexOf(v);
			path.add(legalMoves.get(idx));
			return v;
		}
		else{
			double v = Double.POSITIVE_INFINITY;
			for(Playerstate successor: successors){
				double result = alphaBetaSearch(successor, alpha, beta, currentDepth-1, path);//full re-search
				scores.add(result);
				v = Math.min(v, result);
				if (alpha >= v) break;
				beta= Math.min(beta, v);
			}
			return v;
		}
	}
	
//	public double negaScoutSearch(Playerstate currentState, double alpha, double beta, int currentDepth, ArrayList<Integer> path) 
//	{
//
//		if(currentState.isGoal() || currentDepth == 0) {
//			return (double)(currentState.utility(this));
//		}
//		
//		//System.out.println(currentDepth);
//		//currentState.printBoard();
//		//System.out.println("\n");
//			double b = beta, v = Double.NEGATIVE_INFINITY;//initial window is [-beta,-alpha]
//			
//			ArrayList<Integer> legalMoves = currentState.getLegalMoves();
//			if( legalMoves.size() == 0)
//				legalMoves.add(d);
//	
//			ArrayList<Playerstate> successors = new ArrayList<Playerstate> () ;
//			ArrayList<Double> scores = new ArrayList<Double>() ;
//			for(int move: legalMoves){
//				successors.add(currentState.getSuccessor(move));
//			}
//		for(Playerstate successor: successors){
//			double result = -negaScoutSearch(successor, -b, -alpha, currentDepth-1, path);
//			if ( (alpha<result)&&(result<beta)&&(successor!=successors.get(0)) )
//				result = -negaScoutSearch(successor, -beta, -alpha, currentDepth-1, path);//full re-search
//			scores.add(result);
//			v = Math.max(v, result);
//			if (v>= beta) break;
//			alpha = Math.max(alpha, v);
//			b = alpha + 1;
//		}
//		int idx = scores.indexOf(v);
//		path.add(legalMoves.get(idx));
//		return v;
//
//	}
	
//	public double expectedNegaScoutSearch(Playerstate currentState, double alpha, double beta, int currentDepth, ArrayList<Integer> path) 
//	{
//
//		if(currentState.isGoal() || currentDepth == 0) {
//			return (double)(currentState.utility(this));
//		}
//		
//		//System.out.println(currentDepth);
//		//currentState.printBoard();
//		//System.out.println("\n");
//			double b = beta, v = Double.NEGATIVE_INFINITY;//initial window is [-beta,-alpha]
//			
//			ArrayList<Integer> legalMoves = currentState.getLegalMoves();
//			if( legalMoves.size() == 0)
//				legalMoves.add(d);
//	
//			ArrayList<Playerstate> successors = new ArrayList<Playerstate> () ;
//			ArrayList<Double> scores = new ArrayList<Double>() ;
//			for(int move: legalMoves){
//				successors.add(currentState.getSuccessor(move));
//			}
//		if(currentState.currentPlayer == this){
//			for(Playerstate successor: successors){
//				double result = -expectedNegaScoutSearch(successor, -b, -alpha, currentDepth-1, path);
//				if ( (alpha<result)&&(result<beta)&&(successor!=successors.get(0)) )
//					result = -expectedNegaScoutSearch(successor, -beta, -alpha, currentDepth-1, path);//full re-search
//				scores.add(result);
//				v = Math.max(v, result);
//				if (v>= beta) break;
//				alpha = Math.max(alpha, v);
//				b = alpha + 1;
//			}
//			int idx = scores.indexOf(v);
//			path.add(legalMoves.get(idx));
//			return v;
//		}
//		else{
//			for(Playerstate successor: successors){
//				scores.add(-expectedNegaScoutSearch(successor, -beta, -alpha, currentDepth-1, path));//full re-search
//			}
//			double sum = 0;
//			for(Double d : scores)
//				sum += d;
//			return sum/scores.size();
//		}
//	}
	
	public void printCause() {
	}

}
