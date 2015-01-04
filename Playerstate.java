import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.awt.Point;
public class Playerstate {
    public  Player           	player1;
    public  Player           	player2;
	public 	int 				player1_head_X;
	public 	int 				player1_head_Y;
	public 	int 				player2_head_X;
	public 	int 				player2_head_Y;
    public  boolean          	board[][];
	public  int					floodBoard[][][];
	public  boolean				enemyReached;
    public  Player				currentPlayer;
    
    private boolean				crashed = false;
    /**
     * test
     *
     */

	public Playerstate( Arena arena, Player setCurrentPlayer){
		player1 = arena.player1;
		player2 = arena.player2;
		player1_head_X = player1.x1;
		player1_head_Y = player1.y1;
		player2_head_X = player2.x1;
		player2_head_Y = player2.y1;
		board = copyBoard(arena.board);
		currentPlayer = setCurrentPlayer;
	}
	
	public Playerstate( Playerstate oldPlayerstate){
		player1 = oldPlayerstate.player1;
		player2 = oldPlayerstate.player2;

		player1_head_X = oldPlayerstate.player1_head_X;
		player1_head_Y = oldPlayerstate.player1_head_Y;
		player2_head_X = oldPlayerstate.player2_head_X;
		player2_head_Y = oldPlayerstate.player2_head_Y;
		board = copyBoard(oldPlayerstate.board);
		currentPlayer = oldPlayerstate.currentPlayer;
	}
	
	public Point getEnemyHead(){
		if (currentPlayer==player2){
			return new Point(player1_head_X, player1_head_Y);
		}
		else{
			return new Point(player2_head_X, player2_head_Y);
		}
	}

	public Point getSelfHead(){
		if (currentPlayer==player1){
			return new Point(player1_head_X, player1_head_Y);
		}
		else{
			return new Point(player2_head_X, player2_head_Y);
		}
	}
	
	public Playerstate getSuccessor(int direction){
		int player_head_X, player_head_Y;
		Player nextPlayer;
		if (currentPlayer==player1){
			player_head_X = player1_head_X;
			player_head_Y = player1_head_Y;
			nextPlayer = player2;
		}
		else{
			player_head_X = player2_head_X;
			player_head_Y = player2_head_Y;
			nextPlayer = player1;
		}
		switch ( direction ) {
			case Player.SOUTH:
			    player_head_Y++;
			    if ( player_head_Y >= currentPlayer.y_max ) player_head_Y = 0;
			    break;
			case Player.NORTH:
			    player_head_Y--;
			    if ( player_head_Y < 0 ) player_head_Y = currentPlayer.y_max - 1;
			    break;
			case Player.EAST:
			    player_head_X++;
			    if ( player_head_X >= currentPlayer.x_max ) player_head_X = 0;
			    break;
			case Player.WEST:
			    player_head_X--;
			    if ( player_head_X < 0 ) player_head_X = currentPlayer.x_max - 1;
			    break;
			default:
			    System.out.println( "UH-OH!" );
			    break;
		}
		Playerstate newPlayerstate= new Playerstate(this);
		if(newPlayerstate.board[player_head_X][player_head_Y] == false)
			newPlayerstate.board[player_head_X][player_head_Y] = true;
		else newPlayerstate.crashed = true;
		newPlayerstate.currentPlayer = nextPlayer;
		if (currentPlayer==player1){
			newPlayerstate.player1_head_X = player_head_X ;
			newPlayerstate.player1_head_Y = player_head_Y;
		}
		else{
			newPlayerstate.player2_head_X = player_head_X;
			newPlayerstate.player2_head_Y = player_head_Y;
		}
		return newPlayerstate;
	}
	
	public ArrayList<Integer> getLegalMoves() {
		int player_head_X, player_head_Y;
		if (currentPlayer==player1){
			player_head_X = player1_head_X;
			player_head_Y = player1_head_Y;
		}
		else{
			player_head_X = player2_head_X;
			player_head_Y = player2_head_Y;
		}
		ArrayList<Integer> moves = new ArrayList<Integer>();
        int dx = 0, dy = 0;
        int nextx, nexty;
        for(int i = 0; i < 4; i++){
            switch(i){
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
            nextx = (player_head_X+dx+currentPlayer.x_max) % currentPlayer.x_max;
            nexty = (player_head_Y+dy+currentPlayer.y_max) % currentPlayer.y_max;
            if( board[nextx][nexty] == false){
                moves.add(i);
            }
        }
        
        for(int i=0;i<moves.size();i++){
        	if(moves.get(i)==currentPlayer.d){
        		int temp = moves.get(0);
        		moves.set(0, currentPlayer.d);
        		moves.set(i, temp);
        		break;
        	}
        }
        return new ArrayList<Integer>( moves );
    }
	
	public ArrayList<Integer> getShuffledLegalMoves() {
		int player_head_X, player_head_Y;
		if (currentPlayer==player1){
			player_head_X = player1_head_X;
			player_head_Y = player1_head_Y;
		}
		else{
			player_head_X = player2_head_X;
			player_head_Y = player2_head_Y;
		}
		ArrayList<Integer> moves = new ArrayList<Integer>();
        int dx = 0, dy = 0;
        int nextx, nexty;
        for(int i = 0; i < 4; i++){
            switch(i){
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
            nextx = (player_head_X+dx+currentPlayer.x_max) % currentPlayer.x_max;
            nexty = (player_head_Y+dy+currentPlayer.y_max) % currentPlayer.y_max;
            if( board[nextx][nexty] == false){
                moves.add(i);
            }
            Collections.shuffle(moves);
        }
        return new ArrayList<Integer>( moves );
    }
	public int manhattan(){
		return Math.abs(player1_head_X-player2_head_X)+Math.abs(player1_head_Y-player2_head_Y);
	}
	
	private boolean[][] copyBoard( boolean [][] oldBoard){
		boolean [][] newBoard = new boolean [oldBoard.length][];
		for (int i = 0; i<oldBoard.length; i++) {
			newBoard[i] =  oldBoard[i].clone();
		}
		return newBoard;
	}
	
	public void printBoard( boolean [][] board){
		for (boolean[] u: board) {
		    for (boolean e: u) {
		    	if(e) 	System.out.print("X ");
		    	else 	System.out.print("  ");
		    }
	        System.out.println();
		}
	}
	public void printBoard(){
		printBoard(this.board);
	}
	/*
	 * evaluation and help functions
	 */
	
	public boolean narrowAlley(int direction){
		int player_head_X, player_head_Y;
		if (currentPlayer==player1){
			player_head_X = player1_head_X;
			player_head_Y = player1_head_Y;
		}
		else{
			player_head_X = player2_head_X;
			player_head_Y = player2_head_Y;
		}
		int x_max = currentPlayer.x_max;
		int y_max = currentPlayer.y_max;
        int nextx = 0, nexty = 0;
		boolean front,left_front,right_front;
		switch ( direction ) {
			case Player.NORTH:
	            nextx = (player_head_X + 0) % x_max;
	            nexty = (player_head_Y - 1 + y_max) % y_max;
	    		left_front  = board[(nextx-1+x_max)%x_max][nexty];
	    		right_front = board[(nextx+1)%x_max][nexty];
	            break;
	        case Player.SOUTH:
	            nextx = (player_head_X + 0) % x_max;
	            nexty = (player_head_Y + 1) % y_max;
	    		left_front  = board[(nextx+1)%x_max][nexty];
	    		right_front = board[(nextx-1+x_max)%x_max][nexty];
	            break;
	        case Player.WEST:
	            nextx = (player_head_X - 1 + x_max) % x_max;
	            nexty = (player_head_Y + 0) % y_max;
	    		left_front  = board[nextx][(nexty+1)%y_max];
	    		right_front = board[nextx][(nexty-1+y_max)%y_max];
	            break;
	        case Player.EAST:
	            nextx = (player_head_X + 1) % x_max;
	            nexty = (player_head_Y + 0) % y_max;
	    		left_front  = board[nextx][(nexty-1+y_max)%y_max];
	    		right_front = board[nextx][(nexty+1)%y_max];
	            break;
			default:
			    System.out.println( "UH-OH!" );
	    		front = false;
	    		left_front  = false;
	    		right_front = false;
			    break;
		}
		front = board[nextx][nexty];
		if(left_front && right_front&&(!front)){
			return true;
		}
		return false;
	}
	
	public int getRight(int curretnDirection){
		int rightHandside;
		switch ( curretnDirection ) {
			case Player.NORTH:
				rightHandside = Player.EAST;
	            break;
	        case Player.SOUTH:
				rightHandside = Player.WEST;
	            break;
	        case Player.WEST:
				rightHandside = Player.NORTH;
	            break;
	        case Player.EAST:
				rightHandside = Player.SOUTH;
	            break;
			default:
			    System.out.println( "UH-OH!" );
				rightHandside = curretnDirection;
			    break;
		}
		return rightHandside;
	}
	
	public int getLeft(int curretnDirection){
		int leftHandside;
		switch ( curretnDirection ) {
			case Player.NORTH:
				leftHandside = Player.WEST;
	            break;
	        case Player.SOUTH:
	        	leftHandside = Player.EAST;
	            break;
	        case Player.WEST:
	        	leftHandside = Player.SOUTH;
	            break;
	        case Player.EAST:
	        	leftHandside = Player.NORTH;
	            break;
			default:
			    System.out.println( "UH-OH!" );
			    leftHandside = curretnDirection;
			    break;
		}
		return leftHandside;
	}
	
	public boolean isWall(int X, int Y){
		return board[X][Y];
	}
	
	public boolean isWall(int X, int Y, int direction){
		switch ( direction ) {
			case Player.NORTH:
				return board[X][(Y-1+currentPlayer.y_max)%currentPlayer.y_max];
	        case Player.SOUTH:
	    		return board[X][(Y+1+currentPlayer.y_max)%currentPlayer.y_max];
	        case Player.WEST:
	    		return board[(X-1+currentPlayer.x_max)%currentPlayer.x_max][Y];
	        case Player.EAST:
	    		return board[(X+1+currentPlayer.x_max)%currentPlayer.x_max][Y];
			default:
			    System.out.println( "UH-OH!" );
				return board[X][Y];
		}
	}
	
	public Point lastPosition(int X, int Y, int direction){
		switch ( direction ) {
			case Player.SOUTH:
				return new Point(X,(Y-1+currentPlayer.y_max)%currentPlayer.y_max);
	        case Player.NORTH:
	    		return new Point(X,(Y+1+currentPlayer.y_max)%currentPlayer.y_max);
	        case Player.EAST:
	    		return new Point((X-1+currentPlayer.x_max)%currentPlayer.x_max,Y);
	        case Player.WEST:
	    		return new Point((X+1+currentPlayer.x_max)%currentPlayer.x_max,Y);
			default:
			    System.out.println( "UH-OH!" );
	    		return new Point(X,Y);
		}
	}
	
	public boolean isGoal(){
		return crashed;
	}
	
	public int utility(Player p){
		//make sure someone was crashed before calling this function
		if(crashed){
			if(currentPlayer==p) return 10*inverse_norm();
			else return -10*inverse_norm();
		}
		else{
			if(currentPlayer==p) return inverse_norm()-manhattan();
			else return -inverse_norm()-manhattan();		
		}
	}

	public int evaluation(Player p){
		floodFill();
		int area0 = 0;
		int area1 = 1;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if( floodBoard[i][j][0] < floodBoard[i][j][1])
					area0 += 1;
				else if( floodBoard[i][j][0] > floodBoard[i][j][1])
					area1 += 1;
			}
		}
		if(enemyReached == true){
			if(currentPlayer == p)
				return area0-area1;
			else
				return area1-area0;
		}
		else{
			if(currentPlayer == p)
				return area0;
			else
				return area1;
		}
	}

	/*return number of walls*/
	public int norm(){
		int n = 0;
		for(boolean[] row:board){
			for(boolean e:row){
				if(e) n++;
			}
		}
		return n;
	}
	/*return number of blanks*/
	public int inverse_norm(){
		int n = 0;
		for(boolean[] row:board){
			for(boolean e:row){
				if(!e) n++;
			}
		}
		return n;
	}

	public void floodFill(){
		int x_max = currentPlayer.x_max;
		int y_max = currentPlayer.y_max;
		int x_self = getSelfHead().x;
		int y_self = getSelfHead().y;
		floodBoard = new int[board.length][][];
		for(int i = 0; i < board.length; i++){
			floodBoard[i] = new int[board[i].length][2];
		}
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				floodBoard[i][j][0] = Integer.MAX_VALUE;
				floodBoard[i][j][1] = Integer.MAX_VALUE;
			}
		}
		Node selfNode = new Node(x_self, y_self, 0);
		Node enemyNode = new Node(getEnemyHead(), 0);

		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(selfNode);

		Node tempNode;
		int tempx, tempy, tempd;
	    while((tempNode = queue.poll())!=null){
			tempx = tempNode.point.x;
			tempy = tempNode.point.y;
			tempd = tempNode.depth;
			if( (board[tempx][tempy] == true && tempd != 0 ) || floodBoard[tempx][tempy][0] <= tempd)
				continue;
			floodBoard[tempx][tempy][0] = tempd;
			Node up = new Node(tempx, (tempy-1+y_max)%y_max, tempd+1);
			Node down = new Node(tempx, (tempy+1+y_max)%y_max, tempd+1);
			Node left = new Node((tempx-1+x_max)%x_max, tempy, tempd+1);
			Node right = new Node((tempx+1+x_max)%x_max, tempy, tempd+1);
			queue.offer(up);
			queue.offer(down);
			queue.offer(left);
			queue.offer(right);
		}

		queue.clear();
		queue.offer(enemyNode);
		while((tempNode = queue.poll())!=null){
			tempx = tempNode.point.x;
			tempy = tempNode.point.y;
			tempd = tempNode.depth;
			if( (board[tempx][tempy] == true && tempd != 0 ) || floodBoard[tempx][tempy][1] <= tempd)
				continue;
			floodBoard[tempx][tempy][1] = tempd;
			Node up = new Node(tempx, (tempy-1+y_max)%y_max, tempd+1);
			Node down = new Node(tempx, (tempy+1+y_max)%y_max, tempd+1);
			Node left = new Node((tempx-1+x_max)%x_max, tempy, tempd+1);
			Node right = new Node((tempx+1+x_max)%x_max, tempy, tempd+1);
			queue.offer(up);
			queue.offer(down);
			queue.offer(left);
			queue.offer(right);
		}

		tempx = getEnemyHead().x; 
		tempy = getEnemyHead().y;
		tempd = floodBoard[tempx][tempy][0];
		enemyReached = true;
		try{
			while(tempx != x_self || tempy != y_self){
				if(floodBoard[(tempx - 1 + x_max) % x_max][tempy][0] < tempd){
			//		actionStack.push(new Integer(Player.EAST));
					tempx = (tempx - 1 + x_max) % x_max;
				}
				else if(floodBoard[(tempx + 1 + x_max) % x_max][tempy][0] < tempd){
			//		actionStack.push(new Integer(Player.WEST));
					tempx = (tempx + 1 + x_max) % x_max;
				}
				else if(floodBoard[tempx][(tempy - 1 + y_max) % y_max][0] < tempd){
			//		actionStack.push(new Integer(Player.SOUTH));
					tempy = (tempy - 1 + y_max) % y_max;
				}
				else if(floodBoard[tempx][(tempy + 1 + y_max) % y_max][0] < tempd){
			//		actionStack.push(new Integer(Player.NORTH));
					tempy = (tempy + 1 + y_max) % y_max;
				}
				else{
					//System.err.println("Doesn't find the action list!");
					enemyReached = false;
					break;
				}
				tempd = floodBoard[tempx][tempy][0];
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.exit(2);
		}
	}
}
