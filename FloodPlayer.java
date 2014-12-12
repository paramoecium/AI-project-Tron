/**
 * FloodPlayer class
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
import java.util.*;



public class FloodPlayer extends Player {


    public boolean didsomething = false;
    public Random random;
	int floodBoard[][];
	Stack<Integer> actionStack;
	int step;


    /**
     * MyPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public FloodPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
	name  = n;
	color = c;
	arena = a;
	x_max = x;
	y_max = y;
	player_no = number;
	random = new Random();
	floodBoard = new int[x_max][y_max];
	cleanFloodBoard();
	actionStack = new Stack<Integer> ();
	step = 4;
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
	if(step % 4 == 0){
		flood();
		step = 3;
		return (actionStack.pop().intValue());
	}
	else{
		step--;
		if(actionStack.empty() == false)
			return (actionStack.pop().intValue());
		else{
			flood();
			return (actionStack.pop().intValue());
		}	
	}
    } /* end of whereDoIGo() */


	private void cleanFloodBoard() {
		for(int i = 0; i < x_max; i++){
			for(int j = 0; j < y_max; j++){
				floodBoard[i][j] = 10000;
			}
		}
	}

	private void flood() {
		cleanFloodBoard();
		Playerstate currentState = this.getCurrentState();
		Node selfNode = new Node(x1, y1, 0);
		Point enemyPoint = currentState.getEnemyHead();
		
		System.out.println("x: " + enemyPoint.x + "," + " y: " + enemyPoint.y);
		System.out.println("x: " + x1 + "," + " y: " + y1);
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(selfNode);


		Node tempNode;
		int tempx, tempy, tempd;
		while((tempNode = queue.poll())!=null){
			tempx = tempNode.point.x;
			tempy = tempNode.point.y;
			tempd = tempNode.depth;
			//System.out.println(tempd);
			if( (arena.board[tempx][tempy] == true && tempd != 0 ) || floodBoard[tempx][tempy] <= tempd)
				continue;
			floodBoard[tempx][tempy] = tempd;
			if(tempx == enemyPoint.x && tempy == enemyPoint.y)
				break;
			Node up = new Node(tempx, (tempy-1+y_max)%y_max, tempd+1);
			Node down = new Node(tempx, (tempy+1+y_max)%y_max, tempd+1);
			Node left = new Node((tempx-1+x_max)%x_max, tempy, tempd+1);
			Node right = new Node((tempx+1+x_max)%x_max, tempy, tempd+1);
			queue.offer(up);
			queue.offer(down);
			queue.offer(left);
			queue.offer(right);
		}
		
		tempx = enemyPoint.x; 
		tempy = enemyPoint.y;
		tempd = floodBoard[tempx][tempy];
		while(tempd > 0){
			if(floodBoard[tempx-1][tempy] == tempd-1){
				actionStack.push(new Integer(Player.EAST));
				tempx = tempx - 1;
			}
			else if(floodBoard[tempx+1][tempy] == tempd-1){
				actionStack.push(new Integer(Player.WEST));
				tempx = tempx + 1;
			}
			else if(floodBoard[tempx][tempy-1] == tempd-1){
				actionStack.push(new Integer(Player.SOUTH));
				tempy = tempy - 1;
			}
			else if(floodBoard[tempx][tempy+1] == tempd-1){
				actionStack.push(new Integer(Player.NORTH));
				tempy = tempy + 1;
			}
			else{
				System.err.println("Doesn't find the action list!");
				System.exit(1);
			}
			tempd = tempd - 1;
		}
		

	}

	void printFloodBoard()
	{
		System.out.println("");
		for(int j = 0; j < y_max; j++){
			for(int i = 0; i < x_max; i++){
				System.out.print(floodBoard[i][j]+",");
			}
			System.out.print("\n");
		}
	}

	class Node{
		public Point point;
		public int depth;

		public Node(int x, int y, int d){
			point = new Point(x, y);
			depth = d;
		}

		public Node(Point p, int d){
			point = p;
			depth = d;
		}

	}
} /* end of MYPlayer class */

