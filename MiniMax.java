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



public class MiniMax extends Player {


    public boolean didsomething = false;
    public Random random;


    /**
     * MyPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public MiniMax( String n, Color c, Arena a, int x, int y, byte number ) {
		name  = n;
		color = c;
		arena = a;
		x_max = x;
		y_max = y;
		player_no = number;
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
		double score = minimaxSearch(currentState, 10000, actions);
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
		System.out.println(currentDepth);
		currentState.printBoard();
		System.out.println("\n");

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
	
	public void printCause() {
	}

}
