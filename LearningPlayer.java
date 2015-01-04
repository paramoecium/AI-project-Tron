/**
 * LearningPlayer class
 *
 * originally written spring-2006/sklar for teaching
 *
 * ATTENTION STUDENTS:
 * you need to modify the method called "whereDoIGo()", below
 *
 */

import java.awt.*;
import java.util.*;



public class LearningPlayer extends Player {


    public Random random;
	private ArrayList<Double> theta;
	//private ArrayList<Double> bestTheta;
	private double gamma;
	private double alpha;
	private int livingAward;
	private int featureNum = 6;
	private int totalAward;



    /**
     * LearningPlayer constructor
     *
     * you probably don't need to modify this method!
     *
     */
    public LearningPlayer( String n, Color c, Arena a, int x, int y, byte number ) {
		name  = n;
		color = c;
		arena = a;
		x_max = x;
		y_max = y;
		player_no = number;
		gamma = 0.05;
		alpha = 0.01;
		livingAward = 40;
		totalAward = 0;
		random = new Random();
		theta = new ArrayList<Double>(Collections.nCopies(featureNum, 0.0));
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
		Playerstate currentState = getCurrentState();
		ArrayList<Integer> legalMoves = currentState.getLegalMoves();
		int bestAction = 0;
		double maxQValue = 0;
		double temp;
		for(int action: legalMoves){
			temp = evaluateQ(currentState, action);
			if(temp > maxQValue){
				maxQValue = temp;
				bestAction = action;
			}
		}
		return bestAction ;
    } /* end of whereDoIGo() */

	private void update(Playerstate currentState, int action, Playerstate nextState, int reward){
		ArrayList<Double> features = getFeature(currentState, action);
		double correlation = reward + gamma*getValue(nextState) - evaluateQ(currentState, action);
		//System.out.println(correlation);
		double sum=0;
		for(double featureWeight: theta){
			int idx = theta.indexOf(featureWeight);
			theta.set(idx, featureWeight + features.get(idx)*alpha*correlation );
			sum+=theta.get(idx);
		}
		for(int i=0;i<theta.size();i++){
			theta.set(i, theta.get(i)/sum);
		}
	}

	private double getValue(Playerstate ps){
		ArrayList<Integer> legalMoves = ps.getLegalMoves();
		double maxQValue = 0;
		double temp;
		for(int action: legalMoves){
			temp = evaluateQ(ps, action);
			if(temp > maxQValue){
				maxQValue = temp;
			}
		}
		return maxQValue;
	}

	private double evaluateQ(Playerstate ps, int action){
		double sum = 0.0;
		ArrayList<Double> features = getFeature(ps, action);
		for(double featureWeight: theta){
			int idx = theta.indexOf(featureWeight);
			sum +=  featureWeight*features.get(idx);
		}
		return sum;
	}

	private ArrayList<Double> getFeature(Playerstate ps, int action){
		ArrayList<Double> features = new ArrayList<Double>();
		features.add( (double)ps.manhattan() );
		features.add( (double)ps.evaluation(this) );
		features.add( (double)ps.frontAntenna() );
		features.add( (double)ps.leftAntenna() );
		features.add( (double)ps.rightAntenna() );
		features.add( (double)ps.norm() );
		//System.out.println("features:"+features);
		if (features.size()>featureNum) System.err.print("error");
		if(ps.player1 == this){
			
		}
		else{
		}
		return features;
	}


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
			arena.state = Arena.RESTARTING;
			return;
		}
		int reward = livingAward;
		if(Tron.learning == true){
			Playerstate ps = getCurrentState();
			update(ps, d, ps.getSuccessor(d), reward);
			livingAward*=0.9;
		}		
    } /* end of step() */

	/**
     * restart()
     *
     */
    public void restart( boolean theOtherGuyCrashed ) {
	// default restart method is empty
		if(Tron.learning) {

			if (Tron.numOfTraining <= 0) 
				Tron.learning = false;

			int reward = livingAward;
			if(theOtherGuyCrashed)
				reward = 300;
			else
				reward = -500;
			Playerstate ps = getCurrentState();
			update(ps, d, ps.getSuccessor(d), reward);
			System.out.println("numOfTraining:"+Tron.numOfTraining);
			System.out.print(theta);
		}
		livingAward = 40;
		totalAward = 0;
    } // end of restart()
}
