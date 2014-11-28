/**
 * MyPlayer class
 *
 * originally written spring-2006/sklar for teaching
 *
 * ATTENTION STUDENTS:
 * you need to modify the method called "whereDoIGo()", below
 *
 */

import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Random;



public class MyPlayer extends Player {


    public boolean didsomething = false;
    public Random random;


    /**
     * MyPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public MyPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
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
	// move randomly
	//return( Math.abs( random.nextInt() % 4 ));
	// "tit for tat" player (copy human)
	return( arena.player2.d );

    } /* end of whereDoIGo() */

    
} /* end of MYPlayer class */