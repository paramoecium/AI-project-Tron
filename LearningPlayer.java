/**
 * LearningPlayer class
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



public class LearningPlayer extends Player {


    public Random random;
	private ArrayList<Double> theta;


    /**
     * LearningPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public LearningPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
		name  = n;
		color = c;
		arena = a;
		x_max = x;
		y_max = y;
		player_no = number;
		random = new Random();
		theta = new ArrayList<Double>();

		ArrayList<Double> initFeature = getFeature(getCurrentState(), d);
		for(int i = 0; i < initFeature.size(); i++){
			theta.add(0.0);
		}
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
		// move randomly
		//return( Math.abs( random.nextInt() % 4 ));
		// "tit for tat" player (copy human)
		return( arena.player2.d );
    } /* end of whereDoIGo() */

	private void update(Playerstate currentState, int action, Playerstate nextState, int reward){
	}

	private double evaluateQ(Playerstate ps, int action){
	}

	private ArrayList<Double> getFeature(Playerstate currentState, int action){
	}
}
