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
	//return( arena.player2.d );
		return avoidCollision(x1, y1, d);

    } /* end of whereDoIGo() */

	public int avoidCollision(int currentx, int currenty, int currentDirection) {
		Playerstate p = this.getCurrentState();
		int dx = 0;
		int dy = 0;
		int cnt = 0;
		int d0 = currentDirection;
		int [] rightScore = {-1, 0};
		int [] leftScore = {-1, 0};
		while(cnt < 4) {
			switch (currentDirection){
				case Player.NORTH:
					dx = 0; dy = -1;
					break;
				case Player.SOUTH:
					dx = 0; dy = 1;
					break;
				case Player.WEST:
					dx = -1; dy = 0;
					break;
				case Player.EAST:
					dx = 1; dy = 0;
					break;
			}
			int nextx = (currentx+dx+x_max)%x_max;
			int nexty = (currenty+dy+y_max)%y_max;
			if (arena.board[nextx][nexty] == false) {
				boolean narrow = p.narrowAlley(currentDirection);
				if((currentDirection == d0)&&!narrow)
					return currentDirection;
				else if(rightScore[0] == -1){
					rightScore[0] = currentDirection;
					rightScore[1] = random.nextInt(100)+1;
					if(!narrow) rightScore[1] += 10;
				}
				else{
					leftScore[0] = currentDirection;
					leftScore[1] = random.nextInt(100)+1;
					if(!narrow) leftScore[1] += 10;
				}
			}
			currentDirection++;
			currentDirection %= 4;
			//System.out.println("Turn");
			cnt++;
		}
		if (rightScore[0] == -1)
			return d0;
		else if (leftScore[0] == -1){
			return rightScore[0];
		}
		else{
			if(rightScore[1]>leftScore[1]){
				return rightScore[0];
			}
			else
				return leftScore[0];
		}
	}

	public void printCause() {
	}

} /* end of MYPlayer class */

