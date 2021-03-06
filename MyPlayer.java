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
import java.util.Random;
import java.util.*;



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
    	int next_d = d;
    	next_d = alongWall(x1, y1, d);
    	int next_d_avoidCollision = avoidCollision(x1, y1, d);
		//if(next_d_avoidCollision != d) next_d = next_d_avoidCollision;
		return next_d;

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
	public int alongWall(int currentx, int currenty, int currentDirection) {
		Playerstate p = this.getCurrentState();
		ArrayList<Integer> legalMoves =  p.getLegalMoves();
		int nextMove = -1;
//		if((random.nextInt() % 8)==0){
//			legalMoves =  p.getShuffledLegalMoves();
//		}
		Point lastXY = p.lastPosition(currentx, currenty, currentDirection);
		if (legalMoves.size()>1){
			boolean currentDirectionIsSafe = false;
			for(int i=0;i<legalMoves.size();i++){
				if ( p.isWall(currentx, currenty, legalMoves.get(i)) ||
				     (p.narrowAlley(legalMoves.get(i)) == true) ){
					legalMoves.remove(i);
				}
				else if(legalMoves.get(i)==currentDirection){
					currentDirectionIsSafe = true;
					if(currentDirectionIsSafe) nextMove = currentDirection;
					else nextMove = legalMoves.get(i);
				}
			}
			//System.out.println(legalMoves);
			for(int i=0;i<legalMoves.size();i++){
				if(legalMoves.get(i)==currentDirection){
					if ( p.isWall( currentx, currenty, p.getRight(currentDirection) )){
						nextMove = legalMoves.get(i);
						break;
					}
					else if ( p.isWall( currentx, currenty, p.getLeft(currentDirection) )){
						nextMove = legalMoves.get(i);
						break;
					}
				}
				else{
					if(legalMoves.get(i)==p.getRight(currentDirection)){
						if( p.isWall( (int)lastXY.getX(), (int)lastXY.getY(), p.getRight(currentDirection) )){
							nextMove = legalMoves.get(i);
							break;
						}
					}
					else if(legalMoves.get(i)==p.getLeft(currentDirection)){
	
						if( p.isWall( (int)lastXY.getX(), (int)lastXY.getY(), p.getLeft(currentDirection) )){
						   	nextMove = legalMoves.get(i);
							break;
						}
					}
				}
				nextMove = legalMoves.get(0);
			}
		}
		else if (legalMoves.size()==1){
			nextMove = legalMoves.get(0);
		}
		else if (legalMoves.size()==0){
			nextMove = currentDirection;
		}
		if(nextMove==-1)System.out.println("error");
		return nextMove;
		
	}
	
	public void printCause() {
	}


	public int regionDeterminedDirection(int limit) {
		Playerstate currentState = this.getCurrentState();
		int currentStateX = currentState.currentPlayer.x1;
		int currentStateY = currentState.currentPlayer.y1;
		Node selfNode = new Node(currentStateX, currentStateY, 0, -2);
		Point enemyPoint = currentState.getEnemyHead();
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(selfNode);

		int [][] regionBoard = new int [x_max][y_max];
		for(int i = 0; i < x_max; i++){
			for(int j = 0; j < y_max; j++){
				regionBoard[i][j] = 200;
			}
		}

		Node tempNode;
		int tempx, tempy, tempd, tempsrcd;
		int [] area = new int [4];
		for(int i = 0; i < 4; i++){
			area[i] = 0;
		}
		while((tempNode = queue.poll())!=null){
			tempx = tempNode.point.x;
			tempy = tempNode.point.y;
			tempd = tempNode.depth;
			tempsrcd = tempNode.srcDirection;

			if( (arena.board[tempx][tempy] == true && tempd != 0 ) || regionBoard[tempx][tempy] <= tempd || tempd > limit)
				continue;
			//if(tempx == enemyPoint.x && tempy == enemyPoint.y){
			//	break;
			//}
			regionBoard[tempx][tempy] = tempd;

			switch (tempsrcd) {
				case Player.NORTH:
					area[0] = area[0]+1;
					break;
				case Player.SOUTH:
					area[1] = area[1]+1;
					break;
				case Player.WEST:
					area[2] = area[2]+1;
					break;
				case Player.EAST:
					area[3] = area[3]+1;
					break;
				default:
			}
	
			Node up;
			Node down;
			Node left;
			Node right;
			if(tempsrcd == -2){
				up = new Node(tempx, (tempy-1+y_max)%y_max, tempd+1, Player.NORTH);
				down = new Node(tempx, (tempy+1+y_max)%y_max, tempd+1, Player.SOUTH);
				left = new Node((tempx-1+x_max)%x_max, tempy, tempd+1, Player.WEST);
				right = new Node((tempx+1+x_max)%x_max, tempy, tempd+1, Player.EAST);
			}
			else{
				up = new Node(tempx, (tempy-1+y_max)%y_max, tempd+1, tempsrcd);
				down = new Node(tempx, (tempy+1+y_max)%y_max, tempd+1, tempsrcd);
				left = new Node((tempx-1+x_max)%x_max, tempy, tempd+1, tempsrcd);
				right = new Node((tempx+1+x_max)%x_max, tempy, tempd+1, tempsrcd);
			}	
			queue.offer(up);
			queue.offer(down);
			queue.offer(left);
			queue.offer(right);
		}
	
		int max = 0;
		int id = 0;
		for(int i = 0; i < 4; i++){
			if(max < area[i]){
				max = area[i];
				id = i;
			}
		}

		switch (id) {
			case 0:
				return Player.NORTH;
			case 1:
				return Player.SOUTH;
			case 2:
				return Player.WEST;
			case 3:
				return Player.EAST;
			default:
				return Player.SOUTH;
		}
}


} /* end of MYPlayer class */
