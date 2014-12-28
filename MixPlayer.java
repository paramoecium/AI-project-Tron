/**
 * MixPlayer class
 *
 */

import java.awt.*;
import java.util.*;



public class MixPlayer extends MyPlayer {


    public boolean didsomething = false;
	int floodBoard[][];
	Stack<Integer> actionStack;
	int step;
	int barrier;


    /**
     * MyPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public MixPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
		super(n, c, a, x, y, number); 
		random = new Random();
		floodBoard = new int[x_max][y_max];
		cleanFloodBoard();
		actionStack = new Stack<Integer> ();
		step = 3;
		barrier = Integer.MAX_VALUE;
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
    	if(currentState.manhattan()<5){
			ArrayList<Integer> actions = new ArrayList<Integer> ();
			double score = alphaBetaSearch(currentState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 10, actions);
			if(actions.size() != 0)
				return actions.get(actions.size()-1);
			else{
				System.err.println("Oops!");
				return d;
			}
    	}
    	else{
    		step --;
    		if(step % 2 == 0){
    			actionStack.removeAllElements();
    			flood();
    			if(actionStack.empty() == true) {
    				return avoidCollision(x1, y1, d); 
    			}
    			step = 2;
    			return (actionStack.pop().intValue());
    		}
    		else{
    			if(actionStack.empty() == false)
    				return (actionStack.pop().intValue());
    			else{
    				flood();
    				if(actionStack.empty() == true) {
    					return avoidCollision(x1, y1, d); 
    				}
    				return (actionStack.pop().intValue());
    			}	
    		}
    	}
    } /* end of whereDoIGo() */


	private void cleanFloodBoard() {
		for(int i = 0; i < x_max; i++){
			for(int j = 0; j < y_max; j++){
				floodBoard[i][j] = barrier;
			}
		}
	}

	private void flood() {
		cleanFloodBoard();
		Playerstate currentState = this.getCurrentState();
		int currentStateX = currentState.currentPlayer.x1;
		int currentStateY = currentState.currentPlayer.y1;
		Node selfNode = new Node(currentStateX, currentStateY, 0);
		Point enemyPoint = currentState.getEnemyHead();
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
			if(tempx == enemyPoint.x && tempy == enemyPoint.y){
				break;
			}
			floodBoard[tempx][tempy] = tempd;
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
		try{
			while(tempx != currentStateX || tempy != currentStateY){
				//System.out.println("x:"+tempx+" y:"+tempy);
				//System.out.println(tempd);
				if(floodBoard[(tempx - 1 + x_max) % x_max][tempy] < tempd){
					actionStack.push(new Integer(Player.EAST));
					tempx = (tempx - 1 + x_max) % x_max;
				}
				else if(floodBoard[(tempx + 1 + x_max) % x_max][tempy] < tempd){
					actionStack.push(new Integer(Player.WEST));
					tempx = (tempx + 1 + x_max) % x_max;
				}
				else if(floodBoard[tempx][(tempy - 1 + y_max) % y_max] < tempd){
					actionStack.push(new Integer(Player.SOUTH));
					tempy = (tempy - 1 + y_max) % y_max;
				}
				else if(floodBoard[tempx][(tempy + 1 + y_max) % y_max] < tempd){
					actionStack.push(new Integer(Player.NORTH));
					tempy = (tempy + 1 + y_max) % y_max;
				}
				else{
					//System.err.println("Doesn't find the action list!");
					break;
				}
				tempd = floodBoard[tempx][tempy];
			}
			if(actionStack.empty() == false){
				actionStack.removeElementAt(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			printFloodBoard();
			System.exit(2);
		}
		

	}

	void printFloodBoard()
	{
		Playerstate currentState = this.getCurrentState();
		Point enemyPoint = currentState.getEnemyHead();
		System.out.println(" ");
		for(int j = 0; j < y_max; j++){
			for(int i = 1*x_max/5; i < 1*x_max/3; i++){
				if(i == enemyPoint.x && j == enemyPoint.y )
					System.out.print("{},");
				else if(floodBoard[i][j] == barrier)
					System.out.print("++,");
				else if((floodBoard[i][j]/10) >= 1)
					System.out.print(" "+floodBoard[i][j]+",");
				else
					System.out.print(floodBoard[i][j]+",");
			}
			System.out.print("\n");
		}
	}

	public double alphaBetaSearch(Playerstate currentState, double alpha, double beta, int currentDepth, ArrayList<Integer> path) 
	{

		if(currentState.isGoal() || currentDepth == 0) {
			return (double)(currentState.utility(this));
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
	
	public void printCause() {
	}

} /* end of MYPlayer class */

