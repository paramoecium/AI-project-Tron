/**
 * Player class
 *
 * originally written by Elizabeth Sklar and Pablo Funes, summer 1998
 *
 * modified for teaching spring-2006/sklar
 *
 */


import java.awt.*;
import java.lang.*;
import java.applet.*;



public class Player {

    public String       name;
    public Color        color;
    public Arena        arena;
    public byte         player_no;

    public boolean didsomething  = false;

    public int          x_max;
    public int          y_max;
    public static final int NORTH = 2;
    public static final int EAST  = 1;
    public static final int SOUTH = 0;
    public static final int WEST  = 3;

    public static final int CRASH_DELTA = 20;

    // previous position in each dimension
    public int          x0, y0;
    // current position in each dimension
    public int          x1, y1;
    // direction of movement
    public int          d;
    // last direction of movement
    public int          old_d;
    public boolean      crash;
    public int          score;

    // for graphic2D
    int lineWidth = 7;//must equal to pizelSize

    /**
     * Player constructor
     *
     */
    public Player() {
    } // end of default Player constructor



    /**
     * start()
     *
     */
    public void start() {
	// default start method is empty
    } // end of start()



    /**
     * stop()
     *
     */
    public void stop()  {
	// default stop method is empty 
    } // end of stop()



    /**
     * restart()
     *
     */
    public void restart( boolean theOtherGuyCrashed ) {
	// default restart method is empty
    } // end of restart()



    /**
     * whereDoIGo()
     *
     */
    public int whereDoIGo() {
	// default player is constant
	//System.out.println("Should not be called");
	return d;
    } // end of whereDoIGo()



    /**
     * go()
     *
     */
    public void go( int x,int y ) {
	x0 = x;
	y0 = y;
	x1 = x0;
	y1 = y0;
	old_d = d = SOUTH;
	crash = false;
	arena.board[x0][y0] = true;
    } /* end of go() */



    /**
     * step()
     *
     */
    public void step() {
		if (( d = whereDoIGo()) != old_d ) { 
			if ( old_d == (d + 2) % 4 ){
				d = old_d;
			}
			old_d = d;
		}
		crash = markBoard( d );
		if ( crash ) {
			printCause();
			arena.state = arena.RESTARTING;
		}
    } /* end of step() */



    /**
     * paint()
     *
     */
    public void paint( Graphics g ) {
    int canvasX0 = lineWidth*x0+lineWidth/2;
    int canvasY0 = lineWidth*y0+lineWidth/2;
    int canvasX1 = lineWidth*x1+lineWidth/2;
    int canvasY1 = lineWidth*y1+lineWidth/2;
	if ( crash ) {
	    ((Graphics2D)g).setStroke(new BasicStroke(1));
	    g.setColor( Color.red );
	    g.drawLine( canvasX1-CRASH_DELTA,canvasY1-CRASH_DELTA,
			canvasX1+CRASH_DELTA,canvasY1+CRASH_DELTA );
	    g.drawLine( canvasX1,canvasY1-CRASH_DELTA,canvasX1,canvasY1+CRASH_DELTA );
	    g.drawLine( canvasX1+CRASH_DELTA,canvasY1-CRASH_DELTA,
			canvasX1-CRASH_DELTA,canvasY1+CRASH_DELTA );
	    g.drawLine( canvasX1-CRASH_DELTA,canvasY1,canvasX1+CRASH_DELTA,canvasY1 );
	}
	else {
	    ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
	    g.setColor( color );
	    g.drawLine( canvasX0, canvasY0, canvasX1, canvasY1);
	}
    } /* end of paint() */


    
    /**
     * newPos()
     *
     */
    public void newPos() {
	x0 = x1;
	y0 = y1;
	paint( arena.getGraphics() );
    } /* end of newPos() */   


    
    /**
     * markBoard()
     *
     */
    public boolean markBoard( int direction ) {
	boolean r = false;
	switch ( direction ) {
	case SOUTH:
	    y1++;
	    if ( y1 >= y_max ) {
		y1 = 0;
		y0 = y1;
	    }
	    break;
	case NORTH:
	    y1--;
	    if ( y1 < 0 ) {
		y1 = y_max - 1;
		y0 = y1;
	    }
	    break;
	case EAST:
	    x1++;
	    if ( x1 >= x_max ) {
		x1 = 0;
		x0 = x1;
	    }
	    break;
	case WEST:
	    x1--;
	    if ( x1 < 0 ) {
		x1 = x_max - 1;
		x0 = x1;
	    }
	    break;
	default:
	    System.out.println( "UH-OH!" );
	    break;
	}
	r = arena.board[x1][y1];
	if(!r) {
		arena.board[x1][y1] = true;
	}
	return( r );
    } /* end of markBoard() */


    
    /**
     * tallyWin()
     *
     */
    public void tallyWin() {
	score++;
    } /* end of tallyWin() */


	public Playerstate getCurrentState () {
		Playerstate playerState = new Playerstate (arena, this);
		return playerState;
	}

	public void printCause() {
	}

} /* end of Player class */
