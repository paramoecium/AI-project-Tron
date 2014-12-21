
import java.awt.Point;

public class Node{

	public Point point;
	public int depth;
	public int srcDirection;

	public Node(int x, int y, int d, int srcd){
		point = new Point(x, y);
		depth = d;
		srcDirection = srcd;
	}

	public Node(int x, int y, int d){
		point = new Point(x, y);
		depth = d;
		srcDirection = -2;
	}

	public Node(Point p, int d){
		point = p;
		depth = d;
		srcDirection = -2;
	}
}

