/**
 * Tron class
 *
 * originally written by Elizabeth Sklar and Pablo Funes, summer 1998
 *
 * modified for teaching spring-2006/sklar
 *
 */


import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;


public class Tron extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int NORTH = 2;
    public static final int EAST  = 1;
    public static final int SOUTH = 0;
    public static final int WEST  = 3;
    
    public String idParam;
    public static Arena  arena;
    public static Label  statusLabel;
    public static Button startButton;
    public Button quitButton;
    public JComboBox<String> pickP1Box;
    public JComboBox<String> pickP2Box;
    public static Random random;

    public static String  gpfile = "gp.2220000";
    public static String  nnfile = "nn.700";
    
    public static final int HUMAN 		= 0;
    public static final int LEVEL1    	= 1;
    public static final int LEVEL2    	= 2;
    public static final int MINMAX    	= 3;
    public static final int MIX    		= 4;
    public static final int GP    		= -1;
    public static final int NN    		= -2;
    public static int player1;
    public static int player2;
	public static boolean textMode      = false;
	public static int numOfTraining			= 0;

	public static KeyDemo myKeyDemo;
    
    public int robotScore, humanScore;

    /**
     * main()
     *
     */
    public static void main( String args[] ) { 
	
	final Tron tron = new Tron();
	tron.setTitle( "Tron" );
	
	Tron.player1 = LEVEL1;
	Tron.player2 = HUMAN;

	tron.robotScore = 0;
	tron.humanScore = 0;
	
	int board_X_max = 10;
	int board_Y_max = 10;
	int pixelSize = 20;//Player.linewidth
	Tron.arena = new Arena( tron, board_X_max, board_Y_max, pixelSize);
	Tron.arena.setSize( pixelSize*board_X_max, pixelSize*board_Y_max );
	
	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	tron.setLayout( layout );
	
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 5;
	layout.setConstraints( Tron.arena,c );
	tron.add( Tron.arena );
	
	c.gridwidth = 1;
	c.weightx   = 1;
	c.anchor    = GridBagConstraints.CENTER;
	

    tron.pickP1Box = new JComboBox<String>(new String[]{"LEVEL1","LEVEL2","HUMAN","GP","NN","MINMAX","MIX"});
	tron.pickP1Box.setSelectedItem("MIX");
	player1 = MIX;
	tron.pickP1Box.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent  e) {
        	if (e.getStateChange() == ItemEvent.SELECTED) {
		        String playerType = (String)e.getItem();
		        if (playerType.equals("HUMAN")){
		        	player1 = HUMAN;
		        }
		        else if (playerType.equals("GP")){
		        	player1 = GP;
		        } 
		        else if (playerType.equals("NN")){
		        	player1 = NN;
		        } 
		        else if (playerType.equals("LEVEL1")){
		        	player1 = LEVEL1;
		        } 
		        else if (playerType.equals("LEVEL2")){
		        	player1 = LEVEL2;
		        } 
		        else if (playerType.equals("MINMAX")){
		        	player1 = MINMAX;
		        } 
		        else if (playerType.equals("MIX")){
		        	player1 = MIX;
		        }
		        statusLabel.setText( "Player 1 is "+ playerType );
        	}
        	arena.selectPlayer1( player1 );
//        	if((Tron.arena.player1 != null)&&(Tron.arena.player2 != null)){
//        		startButton.setEnabled(true);
//        	}
        	tron.requestFocusInWindow();
        }
	});
	c.gridx = 0;
	c.gridy = 1;
	layout.setConstraints( tron.pickP1Box,c );
	tron.add( tron.pickP1Box );
	
	tron.pickP2Box = new JComboBox<String>(new String[]{"LEVEL1","LEVEL2","HUMAN","GP","NN","MINMAX","MIX"});
	tron.pickP2Box.setSelectedItem("MINMAX");
	player2 = MINMAX;
	tron.pickP2Box.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent  e) {
        	if (e.getStateChange() == ItemEvent.SELECTED) {
		        String playerType = (String)e.getItem();
		        if (playerType.equals("HUMAN")){
		        	player2 = HUMAN;
		        }
		        else if (playerType.equals("GP")){
		        	player2 = GP;
		        } 
		        else if (playerType.equals("NN")){
		        	player2 = NN;
		        } 
		        else if (playerType.equals("LEVEL1")){
		        	player2 = LEVEL1;
		        } 
		        else if (playerType.equals("LEVEL2")){
		        	player2 = LEVEL2;
		        } 
		        else if (playerType.equals("MINMAX")){
		        	player2 = MINMAX;
		        } 
		        else if (playerType.equals("MIX")){
		        	player2 = MIX;
		        }
		        statusLabel.setText( "Player 2 is "+ playerType );
        	}
        	arena.selectPlayer2( player2 );
//        	if((Tron.arena.player1 != null)&&(Tron.arena.player2 != null)){
//        		startButton.setEnabled(true);
//        	}
        	tron.requestFocusInWindow();
        }
	});
	c.gridx = 1;
	c.gridy = 1;
	layout.setConstraints( tron.pickP2Box,c );
	tron.add( tron.pickP2Box );

	c.gridx = 2;
	c.gridy = 1;
//	layout.setConstraints( tron.pickMyButton,c );
//	tron.add( tron.pickMyButton );
		
	Tron.startButton = new Button( "start" );
	Tron.startButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			arena.startAgain = true;
			tron.requestFocusInWindow();
		}
	});
	c.gridx = 3;
	c.gridy = 1;
	layout.setConstraints( Tron.startButton,c );
	tron.add( Tron.startButton );
//	startButton.setEnabled(false);
	
	tron.quitButton = new Button( "quit" );
	tron.quitButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			System.exit( 0 );
		}
	});
	c.gridx = 4;
	c.gridy = 1;
	layout.setConstraints( tron.quitButton,c );
	tron.add( tron.quitButton );
	
	Tron.statusLabel = new 
	    Label( "select robot opponent, then press 'start'         " );
	c.gridx = 0;
	c.gridy = 2;
	c.gridwidth = 5;
	layout.setConstraints( Tron.statusLabel,c );
	tron.add( Tron.statusLabel );

	tron.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent windowEvent){
			System.exit(0);
		}        
	});  

	Tron.myKeyDemo = new KeyDemo (arena);
	tron.addKeyListener(Tron.myKeyDemo);
	tron.addKeyListener(new KeyListener() {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				arena.startAgain = true;
				tron.requestFocusInWindow();
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}
	});

	if(args.length != 0){
		for(String s: args){
			System.out.println("Run in textMode" + s + "times");
			numOfTraining = Integer.parseInt(s);
		}

		textMode = true;
		arena.startAgain = true;
	}

	if(textMode == false){
		enableDisplay(tron);
	}

	Tron.arena.start();

	arena.selectPlayer1( player1 );
	arena.selectPlayer2( player2 );
	tron.requestFocusInWindow();
    } /* end of main() */

    
    
    /**
     * updateScore()
     *
     */
    public void updateScore() {
		robotScore = arena.player1.score;
		humanScore = arena.player2.score;
		statusLabel.setText( "robot: [" + robotScore + "]  "+  "human: [" + humanScore + "]" );
		System.out.println(  "robot: [" + robotScore + "]  "+  "human: [" + humanScore + "]" ) ;
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

    
	public static void enableDisplay(Tron tron){
		tron.pack();
		tron.setVisible(true);
		tron.setFocusable(true);
	}    
    /**
     * handleEvent()
     *
     *
    public boolean handleEvent( Event evt ) {
	if ( evt.id == Event.WINDOW_DESTROY ) {
	    System.exit( 1 );
	    return true;
	}
	return super.handleEvent( evt );
    } * end of handleEvent() */  

    
    
    /**
     * action()
     *
     *
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
	    startButton.setEnabled(true);
	    return true;
	}
	else if ( arg.equals( "NN robot" )) {
	    statusLabel.setText( "robot will be controlled by NN" );
	    player1 = NN;
	    arena.selectPlayer1( player1,nnfile );
	    startButton.setEnabled(true);
	    return true;
	}
	else if ( arg.equals( "My robot" )) {
	    statusLabel.setText( "robot will be controlled by my code" );
	    player1 = MY;
	    arena.selectPlayer1( player1,nnfile );
	    startButton.setEnabled(true);
	    return true;
	}
	return false;
    }  end of action() */
   

    /**
     * keyDown()
     *
     *
	 *
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
    }   end of keyDown() */

    
} /* end of Tron class */


class KeyDemo extends KeyAdapter {
	Arena arena;

	public KeyDemo (Arena a) {
		arena = a;
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		//System.out.println("keyPressed");
		if(arena.state == Arena.RUNNING) {
			switch ( key ) {
				case KeyEvent.VK_UP:
					arena.player2.d = Tron.NORTH;
					arena.player2.didsomething = true;
					//System.out.println("North");
					break;
				case KeyEvent.VK_DOWN:
					arena.player2.d = Tron.SOUTH;
					arena.player2.didsomething = true;
					//System.out.println("South");
					break;
				case KeyEvent.VK_LEFT:
					arena.player2.d = Tron.WEST;
					arena.player2.didsomething = true;
					//System.out.println("West");
					break;
				case KeyEvent.VK_RIGHT:
					arena.player2.d = Tron.EAST;
					arena.player2.didsomething = true;
					//System.out.println("East");
					break;
			}
		}
		//else{
		//	System.out.println(key);
		//}
	}
	public void keyReleased(KeyEvent e){
	}
	public void keyTyped(KeyEvent e){
	}
}
