/**
 * Tron class
 *
 * originally written by Elizabeth Sklar and Pablo Funes, summer 1998
 *
 * modified for teaching spring-2006/sklar
 *
 */


import java.awt.*;
import java.lang.*;
import java.applet.*;
import java.util.*;


public class Tron extends Frame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NORTH = 2;
    public static final int EAST  = 1;
    public static final int SOUTH = 0;
    public static final int WEST  = 3;
    
    public String idParam;
    public Arena  arena;
    public Label  statusLabel;
    public Button startButton;
    public Button quitButton;
    public Button pickGPButton;
    public Button pickNNButton;
    public Button pickMyButton;
    public static Random random;

    public static String  gpfile = "gp.2220000";
    public static String  nnfile = "nn.700";
    
    public static final int NONE  = -1;
    public static final int HUMAN = 0;
    public static final int GP    = 1;
    public static final int NN    = 2;
    public static final int MY    = 3;
    public static int player1;
    public static int player2;

    
    public int robotScore, humanScore;



    /**
     * main()
     *
     */
    public static void main( String args[] ) { 
	
	Tron tron = new Tron();
	tron.setTitle( "Tron" );
	
	tron.player1 = NONE;
	tron.player2 = HUMAN;

	tron.robotScore = 0;
	tron.humanScore = 0;
	
	tron.arena = new Arena( tron );
	tron.arena.resize( 256, 256 );

	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	tron.setLayout( layout );
	
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 4;
	layout.setConstraints( tron.arena,c );
	tron.add( tron.arena );
	
	c.gridwidth = 1;
	c.weightx   = 1;
	c.anchor    = GridBagConstraints.CENTER;

	tron.pickGPButton = new Button( "GP robot" );
	c.gridx = 0;
	c.gridy = 1;
	layout.setConstraints( tron.pickGPButton,c );
	tron.add( tron.pickGPButton );
	
	tron.pickNNButton = new Button( "NN robot" );
	c.gridx = 1;
	c.gridy = 1;
	layout.setConstraints( tron.pickNNButton,c );
	tron.add( tron.pickNNButton );
	
	tron.pickMyButton = new Button( "My robot" );
	c.gridx = 2;
	c.gridy = 1;
	layout.setConstraints( tron.pickMyButton,c );
	tron.add( tron.pickMyButton );
	
	tron.startButton = new Button( "start" );
	c.gridx = 3;
	c.gridy = 1;
	layout.setConstraints( tron.startButton,c );
	tron.add( tron.startButton );
	tron.startButton.disable();
	
	tron.quitButton = new Button( "quit" );
	c.gridx = 4;
	c.gridy = 1;
	layout.setConstraints( tron.quitButton,c );
	tron.add( tron.quitButton );
	
	tron.statusLabel = new 
	    Label( "select robot opponent, then press 'start'         " );
	c.gridx = 0;
	c.gridy = 2;
	c.gridwidth = 5;
	layout.setConstraints( tron.statusLabel,c );
	tron.add( tron.statusLabel );
	
	tron.pack();
	tron.show();
	tron.arena.start();

    } /* end of main() */

    
    
    /**
     * updateScore()
     *
     */
    public void updateScore() {
	robotScore = arena.player1.score;
	humanScore = arena.player2.score;
	statusLabel.setText( "robot: [" + robotScore + "]  "+
			     "human: [" + humanScore + "]" );
    } /* end of updateScore() */


    
    /**
     * start()
     *
     */
    public void start() {
	arena.start();
    } /* end of start() */

    
    
    /**
     * stop()
     *
     */
    public void stop() {
	arena.stop();
    } /* end of stop() */ 

    
    
    /**
     * destroy()
     *
     */
    public void destroy() {
    } /* end of destroy() */

    
    
    /**
     * handleEvent()
     *
     */
    public boolean handleEvent( Event evt ) {
	if ( evt.id == Event.WINDOW_DESTROY ) {
	    System.exit( 1 );
	    return true;
	}
	return super.handleEvent( evt );
    } /* end of handleEvent() */

    
    
    /**
     * action()
     *
     */
    public boolean action( Event evt,Object arg ) {
	if ( arg.equals( "quit" )) {
	    System.exit( 1 );
	    return true;
	}
	else if ( arg.equals( "start" )) {
	    arena.startAgain = true;
	    return true;
	}
	else if ( arg.equals( "GP robot" )) {
	    statusLabel.setText( "robot will be controlled by GP" );
	    player1 = GP;
	    arena.selectPlayer1( player1,gpfile );
	    startButton.enable();
	    return true;
	}
	else if ( arg.equals( "NN robot" )) {
	    statusLabel.setText( "robot will be controlled by NN" );
	    player1 = NN;
	    arena.selectPlayer1( player1,nnfile );
	    startButton.enable();
	    return true;
	}
	else if ( arg.equals( "My robot" )) {
	    statusLabel.setText( "robot will be controlled by my code" );
	    player1 = MY;
	    arena.selectPlayer1( player1,nnfile );
	    startButton.enable();
	    return true;
	}
	return false;
    } /* end of action() */
    

    
    /**
     * keyDown()
     *
     */
    public boolean keyDown( Event e,int key ) {
	if ( player2 == HUMAN ) {
	    switch ( key ) {
	    case Event.UP:
	    case (int) '8':
	    case (int) 'w':
		arena.player2.d = NORTH;
		arena.player2.didsomething = true;
		break;
	    case Event.DOWN:
	    case (int) '2':
	    case (int) 'z':
		arena.player2.d = SOUTH;
		arena.player2.didsomething = true;
		break;
	    case Event.LEFT:
	    case (int) '4':
	    case (int) 'a':
		arena.player2.d = WEST;
		arena.player2.didsomething = true;
		break;
	    case Event.RIGHT:
	    case (int) '6':
	    case (int) 's':
		arena.player2.d = EAST;
		arena.player2.didsomething = true;
		break;
	    }
	    return true;
	}
	else {
	    return false;
	}
    } /* end of keyDown() */

    
    
} /* end of Tron class */
