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
	public	FloodPlayer		 floodplayer;
	public  HumanPlayer		 humanplayer;
	public	GPPlayer		 gpplayer2;
    public  NNPlayer         nnplayer2;
    public  MyPlayer         myplayer2;
	public	FloodPlayer		 floodplayer2;
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
    
    public int               pixelSize;
    public boolean			 boarderOn = false;
    
    /**
     * Arena constructor
     *
     */
    public Arena( Tron t ,int set_xmax, int set_ymax, int setPixelSize) {
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
	pixelSize = setPixelSize;
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
	floodplayer = new FloodPlayer( "fl",Color.pink,this,xmax,ymax,(byte)1 );
	humanplayer = new HumanPlayer( "human",Color.cyan,this,xmax,ymax,(byte)2 );
	gpplayer2 = new GPPlayer( "gp2",Color.cyan,this,xmax,ymax,(byte)2,null);
	nnplayer2 = new NNPlayer( "nn2",Color.cyan,this,xmax,ymax,(byte)2,null);
	myplayer2 = new MyPlayer( "my2",Color.cyan,this,xmax,ymax,(byte)2);
	floodplayer2 = new FloodPlayer( "fl2",Color.cyan,this,xmax,ymax,(byte)2);
//	player2 = humanplayer;
//	player2.crash = false;
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
    public void selectPlayer1( int player)
	{
		if ( player == Tron.GP ) {
			gpplayer.getStrategy("gp.2220000");
			player1 = gpplayer;
			player1.name = "GP";
		}
		else if ( player == Tron.NN ) {
			nnplayer.readNetwork("nn.700");
			player1 = nnplayer;
			player1.name = "NN";
		}
		else if ( player == Tron.LEVEL1 ) {
			player1 = myplayer;
			player1.name = "MY";
		}
		else if ( player == Tron.LEVEL2 ) {
			player1 = floodplayer;
			player1.name = "FLOOD";
		}
		player1.crash = false;
		clear = true;
		repaint();
    } /* end of selectPlayer1() */
    
	

	/**
     * selectPlayer2()
     *
     * this method is called when the user clicks on a button in the
     * interface and selects a robot controller
     *
     */
    public void selectPlayer2( int player)
	{
		System.out.println(player);
		if ( player == Tron.GP ) {
			gpplayer2.getStrategy("gp.2220000");
			player2 = gpplayer2;
			player2.name = "GP2";
		}
		else if ( player == Tron.NN ) {
			nnplayer2.readNetwork("nn.700");
			player2 = nnplayer2;
			player2.name = "NN2";
		}
		else if ( player == Tron.LEVEL1 ) {
			player2 = myplayer2;
			player2.name = "MY2";
		}
		else if( player == Tron.LEVEL2 ) {
			System.out.println("Select Level2");
			System.out.println(floodplayer2.name);
			player2 = floodplayer2;
			System.out.println(player2.name);
			player2.name = "FLOOD";
		}
		else{
			player2 = humanplayer;
		}
		player2.crash = false;
		clear = true;
		repaint();
    } /* end of selectPlayer2() */



    
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
	    if ( player1 != null && player2 != null) { // hack hack
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
	if ( boarderOn == true ) {
		g.setColor( Color.red.darker().darker().darker() );
		for(int i=0;i<xmax;i++) {
			g.fillRect(pixelSize*i, pixelSize*0, pixelSize, pixelSize);
			g.fillRect(pixelSize*i, pixelSize*(ymax-1), pixelSize, pixelSize);
		}
		for(int i=0;i<ymax;i++) {
			g.fillRect(pixelSize*0, pixelSize*i, pixelSize, pixelSize);
			g.fillRect(pixelSize*(xmax-1), pixelSize*i, pixelSize, pixelSize);
		}
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
	for (int i=0; i<xmax; i++ )
	    for (int j=0; j<ymax; j++ )
		board[i][j] = false;
	clear = true;

	if ( boarderOn == true ) {
		for(int i=0;i<xmax;i++) {
			board[i][0] = true;
			board[i][ymax-1] = true;
		}
		for(int i=0;i<ymax;i++) {
			board[0][i] = true;
			board[xmax-1][i] = true;
		}
	}
    } /* end of clearBoard() */
    


} /* end of Arena class */
