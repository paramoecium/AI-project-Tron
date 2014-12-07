import java.util.ArrayList;
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

    private static final int NORTH = 2;
    private static final int EAST  = 1;
    private static final int SOUTH = 0;
    private static final int WEST  = 3;

    private static int x_max;
    private static int y_max;
    
	public Playerstate( Arena arena, Player setCurrentPlayer){
		player1 = arena.player1;
		player2 = arena.player2;
		board = copyBoard(arena.board);
		currentPlayer = setCurrentPlayer;
		x_max = board.length;
		y_max = board[0].length;
	}
	public Playerstate( Playerstate oldPlayerstate){
		player1 = oldPlayerstate.player1;
		player2 = oldPlayerstate.player2;
		board = copyBoard(oldPlayerstate.board);
		currentPlayer = oldPlayerstate.currentPlayer;
	}
	public Point getEnemyHead(){
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
			player_head_X = player1_head_X;
			player_head_Y = player1_head_Y;
			nextPlayer = player1;
		}
		switch ( direction ) {
			case SOUTH:
			    player_head_Y++;
			    if ( player_head_Y >= y_max ) player_head_Y = 0;
			    break;
			case NORTH:
			    player_head_Y--;
			    if ( player_head_Y < 0 ) player_head_Y = y_max - 1;
			    break;
			case EAST:
			    player_head_X++;
			    if ( player_head_X >= x_max ) player_head_X = 0;
			    break;
			case WEST:
			    player_head_X--;
			    if ( player_head_X < 0 ) player_head_X = x_max - 1;
			    break;
			default:
			    System.out.println( "UH-OH!" );
			    break;
		}
		Playerstate newPlayerstate= new Playerstate(this);
		newPlayerstate.board[player_head_X][player_head_Y] = true;
		newPlayerstate.currentPlayer = nextPlayer;
		if (currentPlayer==player1){
			newPlayerstate.player1_head_X = player_head_X ;
			newPlayerstate.player1_head_Y = player_head_Y;
		}
		else{
			newPlayerstate.player2_head_X = player_head_X;
			newPlayerstate.player2_head_Y = player_head_Y;
			nextPlayer = player1;
		}
		return newPlayerstate;
	}
	public Integer [] getLegalMoves() {
		ArrayList<Integer> moves = new ArrayList<Integer>();
        int dx = 0, dy = 0;
        int nextx, nexty;
        for(int i = 0; i < 4; i++){
            if ( i == ((currentPlayer.d + 2) % 4) ) continue;
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
            nextx = (currentPlayer.x1+dx+currentPlayer.x_max) % currentPlayer.x_max;
            nexty = (currentPlayer.y1+dy+currentPlayer.y_max) % currentPlayer.y_max;
            if( board[nextx][nexty] == false){
                moves.add(i);
            }
        }
        return (Integer[]) moves.toArray();
    }
	private boolean[][] copyBoard( boolean [][] oldBoard){
		boolean [][] newBoard = new boolean [2][];
		for (int i = 0; i<oldBoard.length; i++) newBoard[i] =  oldBoard[i].clone();
		return newBoard;
	}
	
}
