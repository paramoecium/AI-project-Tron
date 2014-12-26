import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;
public class Playerstate {
    public  Player           	player1;
    public  Player           	player2;
	public 	int 				player1_head_X;
	public 	int 				player1_head_Y;
	public 	int 				player2_head_X;
	public 	int 				player2_head_Y;
    public  boolean          	board[][];
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
	
	public Integer [] getLegalMoves() {
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
        return (Integer[]) moves.toArray(new Integer[0]);
    }
	
	public Integer [] getShuffledLegalMoves() {
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
        return (Integer[]) moves.toArray(new Integer[0]);
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
		    	if(e) 	System.out.print("T ");
		    	else 	System.out.print("F ");
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
	
	public boolean isWall(int X, int Y){
		return board[X][Y];
	}
	
	public boolean isGoal(){
		return crashed;
	}
	
	public int utility(Player p){
		//make sure someone was crashed before calling this function
		if(crashed){
			if(currentPlayer==p) return inverse_norm();
			else return -inverse_norm();
		}
		else return 0;		
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
}
