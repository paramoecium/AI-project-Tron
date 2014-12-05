/**
 * Arena class
 *
 * originally written by Elizabeth Sklar and Pablo Funes, summer 1998
 *
 * modified for teaching spring-2006/sklar
 *
 */


import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.Vector;
import java.net.*;
import java.io.*;


public class Arena extends Canvas implements Runnable {
    
    Dimension grayDimension;
    Image     grayImage;
    Graphics  grayGraphics;
    
    public Tron              tron;
    
    private Thread           conductor;
    public  Player           player1;
    public  Player           player2;
    public  GPPlayer         gpplayer;
    public  NNPlayer         nnplayer;
    public  MyPlayer         myplayer;
    public  GPPlayer         gpplayer2;
    public  boolean          board[][];
    public  boolean          clear;
    public  boolean          startAgain = false;
    private int              xmax, ymax;
    
    public static final int  WAITING = 0;
    public static final int  RUNNING = 1;
    public static final int  RESTARTING = 2;
    public int               state;
    
    public int               lastmove;
    public int               gen_no;
    
    
    
    /**
     * Arena constructor
     *
     */
    public Arena( Tron t ,int set_xmax, int set_ymax) {
	setBackground( Color.black );
	player1 = null;
	player2 = null;
	conductor = null;
	board = null;
	state = WAITING;
	tron = t;
	gen_no = 0;
	xmax = set_xmax;
	ymax = set_ymax;
    } /* end of Arena constructor */


    
    /**
     * start()
     *
     * this method is called once, when the arena is initialized and
     * its runnable thread starts
     *
     */
    public void start() {
	if ( board == null ) {
	    board = new boolean[xmax][ymax];
	}
	gpplayer = new GPPlayer( "gp",Color.pink,this,xmax,ymax,(byte)1,null );
	nnplayer = new NNPlayer( "nn",Color.pink,this,xmax,ymax,(byte)1,null );
	myplayer = new MyPlayer( "my",Color.pink,this,xmax,ymax,(byte)1 );
	//gpplayer2 = new GPPlayer( "GP2",Color.cyan,this,xmax,ymax,(byte)2,null );
	player2 = new HumanPlayer( "human",Color.cyan,this,xmax,ymax,(byte)2 );
	if ( grayImage != null ) {
	    this.getGraphics().drawImage( grayImage,0,0,this ); 
	}
	if ( conductor == null ) {
	    conductor = new Thread( this,"Arena" );
	    conductor.start();
	}
	else {
		synchronized(this){
			if(conductor.getState() == Thread.State.WAITING)
				conductor.notify();
		}
	}
    } /* end of start() */


    
    /**
     * stop()
     *
     * this method is called when the runnable thread stops
     *
     */
    public void stop() {
		synchronized(this) {
			try{
				conductor.wait();
			}catch(InterruptedException e){
				System.err.println("Suspend thread error");
				e.printStackTrace();
			}
        }
    } /* end of stop() */

    
    
    /**
     * selectPlayer1()
     *
     * this method is called when the user clicks on a button in the
     * interface and selects a robot controller
     *
     */
    public void selectPlayer1( 
			      int player,
			      String filename
			      ) {
	if ( player == Tron.GP ) {
	    gpplayer.getStrategy( filename );
	    player1 = gpplayer;
	    player1.name = "GP";
	}
	else if ( player == Tron.NN ) {
	    nnplayer.readNetwork( filename );
	    player1 = nnplayer;
	    player1.name = "NN";
		//gpplayer2.getStrategy( "gp.2220000" );
		//player2 = gpplayer2;
		//player2.name = "GP2";
	}
	else if ( player == Tron.MY ) {
	    player1 = myplayer;
	    player1.name = "MY";
		//gpplayer2.getStrategy( "gp.2220000" );
		//player2 = gpplayer2;
		//player2.name = "GP2";
	}
	player1.crash = false;
	player2.crash = false;
	clear = true;
	repaint();
    } /* end of selectPlayer1() */
    

    
    /**
     * startPlayers()
     *
     * this method is called when the user clicks on the "start"
     * button in the interface
     *
     */
    public void startPlayers() {
	clearBoard();
	player1.go( xmax/4,ymax/2 );
	player2.go( 3*xmax/4,ymax/2 );
	player1.score = tron.robotScore;
	state = RUNNING;
	lastmove = 0;
    } /* end of startPlayers() */
    

    
    /**
     * run()
     *
     * this is the "run" method that executes forever while the
     * runnable thread is executing
     *
     */
    public void run() {
	while ( true ) {
	    switch ( state ) {
	    case RUNNING:
		player1.step();
		player2.step();
		break;
	    case RESTARTING:
		if ( player1.crash && player2.crash ) {
		    // tie
		}
		else if ( player1.crash ) {
		    // player1 crashes -> player2 wins -> result="L"
		    player2.tallyWin();
		}
		else if ( player2.crash ) {
		    // player2 crashes -> player1 wins -> result="W"
		    player1.tallyWin();
		}
		player1.restart( player2.crash );
		player2.restart( player1.crash );
		state = WAITING;
		tron.updateScore();
		break;
	    case WAITING:
		if ( startAgain ) {
		    startAgain = false;
		    start();
		    startPlayers();
		}
		break;
	    }
	    repaint();
	    try { 
		Thread.sleep( 30 );
	    }
	    catch ( InterruptedException e ) {
	    }
	    if ( player1 != null ) { // hack hack
		player1.newPos();
		player2.newPos();
	    }
	}
    } /* end of run() */
    
    

    /**
     * update()
     *
     * this method is called by the native paint() method to draw on
     * this canvas
     *
     */
    public void update( Graphics g ) {
	if ( clear ) {
	    g.clearRect( 0,0,getSize().width,getSize().height );
	    clear = false;
	}
	if ( player1 != null ) {
	    player1.paint( g ); 
	}
	if ( player2 != null ) {
	    player2.paint( g );
	}
    } /* end of update() */

    
    
    /**
     * clearBoard()
     *
     * this method is called after a game ends to clear out the board
     *
     */
    public void clearBoard() {
	int i, j;
	for ( i=0; i<xmax; i++ )
	    for ( j=0; j<ymax; j++ )
		board[i][j] = false;
	clear = true;
    } /* end of clearBoard() */



} /* end of Arena class */
